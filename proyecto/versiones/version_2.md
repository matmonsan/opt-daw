---
title: Versión 2
description: "<strong>Módulo:</strong> Programación Web Avanzada <br> <strong>Profesor:</strong> Matías Montávez Sánchez"
---
[⌂ Volver al enunciado del proyecto](../enunciado_proyecto.md)

# **FECHA DE ENTREGA:** 13/12/2026

# Versión 2 — Persistencia con Spring Data JPA y Lombok

## Objetivo

Sustituir los datos en memoria de los servicios por una base de datos relacional gestionada con Spring Data JPA: el modelo pasa a ser un conjunto de entidades con sus relaciones, se añade la capa de repositorios con Spring Data y Lombok elimina el código repetitivo. Además, se validan los datos de entrada, los filtros se trasladan a consultas, los listados se paginan, las operaciones críticas se hacen transaccionales, se añaden estadísticas de ventas, la importación de pedidos desde Google Sheets y la documentación de la API (<span class="investigacion">investigación</span>).

Los *endpoints* de la Versión 1 deben seguir funcionando igual y con la misma seguridad. Solo cambian dos cosas: los listados pasan a estar paginados (apartado 6) y los datos de entrada se validan (apartado 9). A ellos se suman los *endpoints* nuevos de esta versión. La colección de Postman de la Versión 1, adaptada a la paginación, es la prueba de que nada se ha roto.

## Planificación orientativa

| Semana | Trabajo |
|---|---|
| 30/11 - 06/12 | Apartados 1 a 4, 9 y comienzo del 10: base de datos, entidades, relaciones, repositorios, validación e importación |
| 07/12 - 13/12 | Apartados 5 a 8, final del 10 y 11: consultas, paginación, transacciones, estadísticas, importación y documentación |

---

## Enunciado

### 1. Base de datos y configuración

Usaréis MariaDB, MySQL o PostgreSQL (si usáis Supabase para la autenticación, podéis usar su base de datos). Cread en la raíz del repositorio un `docker-compose.yml` que levante la base de datos. Ejemplo con MariaDB:

```yaml
services:
  db:
    image: mariadb:11.4
    environment:
      MARIADB_DATABASE: tienda
      MARIADB_USER: tienda
      MARIADB_PASSWORD: ${DB_PASSWORD}
      MARIADB_ROOT_PASSWORD: ${DB_ROOT_PASSWORD}
    ports:
      - "3306:3306"
    volumes:
      - datos_tienda:/var/lib/mysql

volumes:
  datos_tienda:
```

Añadid las dependencias Spring Data JPA, Lombok y el *driver* de la base de datos, y configurad la conexión leyendo las contraseñas de variables de entorno (archivo `.env` fuera del repositorio y `.env.example` dentro):

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/tienda
spring.datasource.username=${DB_USER:tienda}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 2. Entidades con Lombok

Transformad las clases del modelo en entidades JPA:

```java
@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String sku;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @Builder.Default
    @Column(nullable = false)
    private boolean activo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    // resto de atributos...
}
```

- Usad `@Getter`, `@Setter`, `@NoArgsConstructor` (JPA lo necesita), `@AllArgsConstructor` y `@Builder`, y `@Builder.Default` para los valores por defecto y las colecciones.
- No uséis `@Data` en entidades con relaciones: los `toString`, `equals` y `hashCode` generados recorren las relaciones y pueden provocar recursión infinita o cargar colecciones sin querer. Explicadlo en la memoria.
- Los enumerados se guardan con `@Enumerated(EnumType.STRING)`.

### 3. Relaciones

| Relación | Tipo | Detalles |
|---|---|---|
| Producto → Categoría | `@ManyToOne` | `LAZY` |
| Usuario ↔ Carrito | `@OneToOne` | El carrito tiene la clave ajena del usuario |
| Carrito → LineaCarrito | `@OneToMany` | `cascade = CascadeType.ALL`, `orphanRemoval = true` |
| LineaCarrito → Producto | `@ManyToOne` | |
| Pedido → Usuario | `@ManyToOne` | |
| Pedido → LineaPedido | `@OneToMany` | `cascade = CascadeType.ALL`, `orphanRemoval = true` |
| LineaPedido → Producto | `@ManyToOne` | Además guarda la copia del nombre y del precio |

En las relaciones bidireccionales, cread métodos que mantengan los dos lados sincronizados (por ejemplo, `pedido.agregarLinea(linea)`). Guardad el diagrama entidad-relación en `docs/diagramas/`.

### 4. Repositorios y servicios

Añadid la capa de repositorio (paquete `repository`): una interfaz por entidad que extienda `JpaRepository`. Los servicios dejan de guardar los datos en colecciones en memoria y pasan a usar los repositorios, que reciben por inyección de dependencias. El flujo queda `Controller → Service → Repository → base de datos`.

```java
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
```

- Los servicios reciben los repositorios en el constructor; a partir de esta versión podéis generar ese constructor con `@RequiredArgsConstructor` de Lombok.
- Las entidades no salen de los servicios: los controladores siguen usando DTOs.
- La conversión a DTO se hace dentro de los métodos del servicio, para evitar la `LazyInitializationException`.
- Los usuarios se guardan en la base de datos la primera vez que acceden a la API.
- Los datos de ejemplo solo se cargan si la base de datos está vacía (`count() == 0`).

### 5. Consultas

Sustituid los filtros con *streams* de la Versión 1 por consultas a la base de datos:

Consultas derivadas, donde sea posible:

```java
Optional<Producto> findBySkuIgnoreCase(String sku);
List<Producto> findByActivoTrueAndDestacadoTrue();
List<Producto> findByActivoTrueAndStockLessThanEqualOrderByStockAsc(int umbral);
Optional<Pedido> findByIdAndUsuarioId(Long id, Long usuarioId);
```

Consultas JPQL con `@Query` para los filtros opcionales combinables del catálogo y de los pedidos de administración:

```java
@Query("""
        SELECT p FROM Producto p
        WHERE p.activo = true
          AND (:categoriaId IS NULL OR p.categoria.id = :categoriaId)
          AND (:precioMin IS NULL OR p.precio >= :precioMin)
          AND (:precioMax IS NULL OR p.precio <= :precioMax)
        """)
Page<Producto> buscar(@Param("categoriaId") Long categoriaId,
                      @Param("precioMin") BigDecimal precioMin,
                      @Param("precioMax") BigDecimal precioMax,
                      Pageable pageable);
```

Completad la consulta con la búsqueda por texto, `soloDisponibles` y vuestro filtro propio, y comprobad en el *log* el SQL que se genera.

### 6. Paginación

Paginad con `Pageable` los listados de productos (público y de administración), pedidos (cliente y administración) y usuarios:

- Parámetros `page`, `size` y `sort` (`?page=0&size=12&sort=precio,desc`). Tamaño por defecto 12 y máximo 50.
- El parámetro `sort` sustituye a los parámetros `orden` y `direccion` de la Versión 1.
- Respuesta con un DTO propio en lugar del `Page` de Spring:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginaDto<T> {
    private List<T> contenido;
    private int pagina;
    private int tamano;
    private long totalElementos;
    private int totalPaginas;
}
```

Actualizad el contrato de la API y la colección de Postman.

### 7. Transacciones

- Los servicios usan `@Transactional` y `@Transactional(readOnly = true)` en las consultas.
- Confirmar un pedido, cancelarlo e importar desde Google Sheets en modo estricto (apartado 10) son atómicos: demostrad que, si falla el último producto de un pedido por falta de stock, no se descuenta el stock de los anteriores ni se crea el pedido.

### 8. Estadísticas de ventas

Cread `GET /api/admin/estadisticas?desde=&hasta=` (por defecto, los últimos 30 días), que usará el panel de administración de la Versión 4:

```json
{
  "totalVentas": 1849.70,
  "numeroPedidos": 32,
  "ticketMedio": 57.80,
  "pedidosPorEstado": { "PENDIENTE": 3, "PAGADO": 5, "ENVIADO": 4, "ENTREGADO": 17, "CANCELADO": 3 },
  "ventasPorCategoria": [ { "categoria": "Rol", "unidades": 41, "importe": 920.50 } ],
  "productosMasVendidos": [ { "sku": "NSW-MK8", "nombre": "Mario Kart 8 Deluxe", "unidades": 18, "importe": 899.82 } ]
}
```

Las ventas excluyen los pedidos cancelados y se calculan en la base de datos con consultas JPQL de agregación (`SUM`, `COUNT`, `GROUP BY`), recogiendo los resultados en DTOs con expresiones `SELECT new ...`.

### 9. Validación de los datos de entrada

Añadid la dependencia Validation (`spring-boot-starter-validation`) y validad todos los datos que llegan a la API:

- Todos los DTOs de entrada tienen restricciones de Bean Validation (`@NotBlank`, `@NotNull`, `@Size`, `@Positive`, `@PositiveOrZero`, `@Pattern`, `@Email`...) con mensajes en castellano pensados para el usuario final.
- Los controladores validan el cuerpo de las peticiones con `@Valid`. Si los datos no cumplen las restricciones, Spring responde `400` sin ejecutar el método.
- Validad también los parámetros de ruta y de consulta que lo necesiten (por ejemplo, `@Min(0)` en el umbral de stock bajo), anotando el controlador con `@Validated`.
- Configurad `server.error.include-binding-errors=always` para que la respuesta incluya los errores de cada campo, que la aplicación Vue mostrará junto al formulario.

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCrearDto {

    @NotBlank(message = "El SKU es obligatorio")
    @Pattern(regexp = "[A-Za-z0-9-]{3,20}", message = "El SKU solo admite letras, números y guiones (3 a 20)")
    private String sku;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    private BigDecimal precio;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stock;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    // resto de atributos...
}
```

Bean Validation comprueba el formato de los datos; las reglas de negocio (SKU único, stock suficiente, transiciones de estado...) siguen en los servicios.

Material de referencia: [apartado 7 de los apuntes](../../aplicaciones.md#validación-de-los-datos-de-entrada).

### 10. Investigación: importar pedidos desde Google Sheets
{: .investigacion}

> Este apartado no se explica en clase. Es trabajo de investigación y debe documentarse en la memoria.
{: .investigacion}

El cliente prepara su pedido en una hoja de Google Sheets compartida como «Cualquier persona con el enlace» → Lector y envía solo el enlace. La hoja tiene una fila de cabecera con las columnas `sku` y `cantidad` (obligatorias, en cualquier orden) y `comentario` (opcional):

| sku | cantidad | comentario |
|---|---|---|
| NSW-MK8 | 2 | Para el cumpleaños |
| PS5-GOW | 1 | |

| Método y ruta | Cuerpo | Descripción |
|---|---|---|
| `POST /api/carrito/importar/previsualizar` | `{ "url": "https://docs.google.com/spreadsheets/d/.../edit#gid=0" }` | Valida la hoja y devuelve el resultado sin modificar el carrito |
| `POST /api/carrito/importar` | `{ "url": "...", "modo": "SUMAR", "estricto": false }` | Importa en el carrito. `modo`: `SUMAR` o `REEMPLAZAR`. Con `estricto`, si alguna fila falla no se importa nada |

La respuesta indica las líneas importadas y los errores fila a fila:

```json
{
  "filasLeidas": 4,
  "lineasImportadas": 2,
  "errores": [
    { "fila": 4, "sku": "XBX-HALO", "motivo": "Solo hay 1 unidad disponible y se han pedido 3" },
    { "fila": 5, "sku": "ABC-999", "motivo": "No existe ningún producto con ese SKU" }
  ],
  "carrito": { "...": "estado resultante del carrito" }
}
```

Pasos a investigar e implementar:

1. Validar el enlace con una expresión regular y extraer el identificador del documento y el `gid` de la pestaña. Solo se aceptan enlaces `https://docs.google.com/spreadsheets/d/<ID>/...` (`400`).
2. Construir la URL de exportación: `https://docs.google.com/spreadsheets/d/<ID>/export?format=csv&gid=<GID>`.
3. Descargar con `RestClient`, con tiempos de espera y comprobando que sigue la redirección de Google. Si la hoja no es pública, Google no devuelve un CSV (`422`); si no responde, `502`.
4. Interpretar el CSV con una librería (Apache Commons CSV u OpenCSV), no con `split(",")`.
5. Validar cada fila (SKU existente y activo, cantidad entera positiva, stock) reutilizando las reglas del carrito. La importación en modo estricto debe ser transaccional (apartado 7).
6. Seguridad (SSRF): la URL que se descarga la construye vuestro código a partir del identificador, nunca se descarga la URL recibida. Limitad el tamaño y el número de filas.

Cread hojas de prueba (correcta, con errores, sin la columna `cantidad` y privada) y enlazadlas en el `README.md` y en Postman.

### 11. Investigación: documentación con OpenAPI y pruebas en Postman
{: .investigacion}

> Este apartado no se explica en clase. Es trabajo de investigación y debe documentarse en la memoria.
{: .investigacion}

- Añadid springdoc-openapi (comprobad la versión compatible con vuestro Spring Boot). Swagger UI (`/swagger-ui.html`) será público, con un esquema de seguridad *Bearer* para probar los *endpoints* con el botón *Authorize*. Agrupad los *endpoints* con `@Tag` y documentadlos con `@Operation`, `@ApiResponse` y `@Schema` con ejemplos.
- Añadid a cada petición de Postman *scripts* de prueba sobre el código y el contenido de la respuesta, encadenad las peticiones con variables y comprobad que la colección se ejecuta completa con el *Collection Runner*.

```javascript
pm.test("Devuelve 201", () => pm.response.to.have.status(201));
pm.environment.set("productoId", pm.response.json().id);
```

---

## Requisitos

- La base de datos se levanta con `docker compose up -d` y los servicios ya no guardan datos en memoria.
- Entidades con Lombok (sin `@Data`) y todas las relaciones de la tabla.
- Las entidades no se exponen en la API.
- Los filtros se resuelven con consultas derivadas y `@Query`, no en memoria.
- Listados paginados con `PaginaDto` y tamaño máximo.
- Operaciones críticas transaccionales, con el *rollback* demostrado.
- Estadísticas calculadas con consultas de agregación.
- Todos los DTOs de entrada validados con Bean Validation y mensajes en castellano.
- Importación desde Google Sheets con previsualización, modos, errores por fila y medidas contra SSRF.
- Swagger UI completo y colección de Postman con pruebas que se ejecuta entera sin fallos.
- Los datos se conservan al reiniciar y la colección de Postman de la Versión 1, adaptada a la paginación, sigue funcionando.
- Etiqueta `v2` en `main`.

## Reparto en la pareja

| Persona A | Persona B |
|---|---|
| `docker-compose.yml` y configuración | Diagrama entidad-relación |
| Entidades y repositorios de catálogo y usuarios | Entidades y repositorios de carrito y pedidos |
| Consultas y paginación del catálogo | Consultas y paginación de pedidos y usuarios |
| Estadísticas de ventas | Transacciones y demostración del *rollback* |
| Validación de los DTOs de catálogo y usuarios | Validación de los DTOs de carrito, pedidos e importación |
| Importación: enlace, descarga y SSRF | Importación: CSV, validación por fila y modos |
| Swagger | Pruebas automáticas de Postman |

## Criterios de finalización

- Toda la API funciona igual que en la Versión 1 (con los listados paginados), ahora con base de datos, y los datos sobreviven a un reinicio.
- `GET /api/productos?categoriaId=2&precioMax=60&page=1&size=5&sort=precio,asc` devuelve la página correcta con los totales.
- Un pedido que falla a mitad no deja cambios en la base de datos.
- Los datos no válidos se rechazan con `400` indicando qué campos fallan y por qué.
- Las cifras de las estadísticas coinciden con una consulta SQL hecha a mano.
- Una hoja de Google Sheets correcta se convierte en el carrito; una con errores informa de cada fila; una privada o un enlace que no es de Google Sheets se rechazan.
- Toda la API se puede probar desde Swagger UI y el *Collection Runner* ejecuta la colección sin fallos.

## Entrega

Incluid en la memoria:

- Diagrama entidad-relación y explicación de cada relación (tipo, lado propietario, `cascade`, `fetch`).
- Anotaciones de Lombok utilizadas y por qué no se usa `@Data`.
- Las consultas `@Query` y el SQL que generan.
- Capturas de un listado paginado, de la demostración del *rollback* y de las estadísticas comprobadas con SQL.
- Capturas de las tablas con datos.
- Capturas de errores de validación con los mensajes de cada campo.
- Investigación sobre la importación: exportación CSV de Google Sheets, `RestClient`, librería CSV, qué es SSRF y cómo se evita, y capturas con cada hoja de prueba.
- Investigación sobre OpenAPI y Postman: capturas de Swagger UI y del *Collection Runner*.

Consultad los requisitos comunes de entrega en el [enunciado general](../enunciado_proyecto.md#contenido-de-cada-entrega).

---

[← Versión 1](version_1.md) · [Versión 3 →](version_3.md)
