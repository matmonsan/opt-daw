---
title: Versión 2
description: "<strong>Módulo:</strong> Programación Web Avanzada <br> <strong>Profesor:</strong> Matías Montávez Sánchez"
---
[⌂ Volver al enunciado del proyecto](../enunciado_proyecto.md)

# **FECHA DE ENTREGA:** 13/12/2026

# Versión 2 — Persistencia con Spring Data JPA y Lombok

## Objetivo

Sustituir los datos en memoria de los servicios por una base de datos MariaDB gestionada con Spring Data JPA: el modelo pasa a ser un conjunto de entidades con sus relaciones, se añade la capa de repositorios con Spring Data y Lombok elimina el código repetitivo. Además, se validan los datos de entrada, los filtros se trasladan a consultas, las operaciones críticas se hacen transaccionales, se añaden estadísticas de ventas, la importación de pedidos desde Google Sheets y la documentación de la API (<span class="investigacion">investigación</span>).

Los *endpoints* de la Versión 1 deben seguir funcionando igual y con la misma seguridad. El único cambio es que los datos de entrada se validan (apartado 8). A ellos se suman los *endpoints* nuevos de esta versión. La colección de Postman de la Versión 1 es la prueba de que nada se ha roto.

## Planificación orientativa

| Semana | Trabajo |
|---|---|
| 30/11 - 06/12 | Apartados 1 a 4, 8 y comienzo del 9: base de datos, entidades, relaciones, repositorios, validación e importación |
| 07/12 - 13/12 | Apartados 5 a 7, final del 9 y 10: consultas, transacciones, estadísticas, importación y documentación |

---

## Enunciado

### 1. Base de datos y configuración

La base de datos será MariaDB, la que incluye XAMPP:

- Arrancad MySQL/MariaDB desde el panel de control de XAMPP y cread una base de datos vacía llamada `tienda` (por ejemplo, desde phpMyAdmin). Las tablas las creará Hibernate.
- Añadid al proyecto las dependencias Spring Data JPA, Lombok y el *driver* de MariaDB.
- Configurad la conexión en `application.properties`:

```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/tienda
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

- Si vuestro usuario de MariaDB tiene contraseña, no la subáis al repositorio: incluid un `application-example.properties` y explicad en el `README.md` cómo configurarla.
- Explicad también en el `README.md` cómo crear la base de datos para arrancar el proyecto.

### 2. Entidades con Lombok

Transformad las clases del modelo en entidades JPA:

- Anotad cada clase con `@Entity` y `@Table`, el identificador con `@Id` y `@GeneratedValue(strategy = GenerationType.IDENTITY)`, y los atributos con `@Column` indicando si son obligatorios (`nullable`), únicos (`unique`, por ejemplo el SKU o el nombre de la categoría) y su longitud (`length`).
- Los importes (`BigDecimal`) se definen con `precision` y `scale` para guardar dos decimales.
- Los enumerados (`Rol`, `EstadoPedido`) se guardan con `@Enumerated(EnumType.STRING)`.
- Usad Lombok para eliminar el código repetitivo: `@Getter`, `@Setter`, `@NoArgsConstructor` (JPA lo necesita) y `@AllArgsConstructor`.
- No uséis `@Data` en entidades con relaciones: los `toString`, `equals` y `hashCode` generados recorren las relaciones y pueden provocar recursión infinita o cargar colecciones sin querer. Explicadlo en la memoria.

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

Los filtros y búsquedas que en la Versión 1 se hacían con *streams* pasan a resolverse en la base de datos. Usad consultas derivadas (el nombre del método genera la consulta) siempre que sea posible, y consultas JPQL con `@Query` cuando haya filtros opcionales que se combinan.

Como mínimo, los repositorios deben ofrecer estas consultas:

| Repositorio | Consulta |
|---|---|
| Productos | Buscar un producto por su SKU sin distinguir mayúsculas y minúsculas |
| Productos | Obtener los productos activos y destacados |
| Productos | Obtener los productos activos de una categoría |
| Productos | Obtener los productos activos con un stock menor o igual que un umbral, ordenados por stock |
| Productos | Comprobar si existe algún producto de una categoría (para impedir borrarla) |
| Productos | Listado del catálogo con los filtros opcionales y combinables de la Versión 1 (categoría, texto, rango de precio, solo disponibles y vuestro filtro propio) y su ordenación |
| Categorías | Comprobar si ya existe una categoría con un nombre |
| Usuarios | Buscar un usuario por su `uid` (o por su correo, si usáis Spring Security) |
| Carritos | Obtener el carrito de un usuario |
| Pedidos | Obtener los pedidos de un usuario, del más reciente al más antiguo, con filtro opcional por estado |
| Pedidos | Obtener un pedido por su identificador solo si pertenece a un usuario (así se devuelve `404` si es de otro) |
| Pedidos | Listado de administración con filtros opcionales por estado, rango de fechas y correo del cliente |

Comprobad en el *log* (`show-sql`) el SQL que genera cada consulta.

### 6. Transacciones

- Los servicios usan `@Transactional` y `@Transactional(readOnly = true)` en las consultas.
- Confirmar un pedido, cancelarlo e importar desde Google Sheets en modo estricto (apartado 9) son atómicos: demostrad que, si falla el último producto de un pedido por falta de stock, no se descuenta el stock de los anteriores ni se crea el pedido.

### 7. Estadísticas de ventas

El panel de administración de la Versión 4 mostrará un resumen de cómo va la tienda en un periodo de tiempo. Para ello, cread el *endpoint* `GET /api/admin/estadisticas?desde=&hasta=`, solo para administradores, que devuelva estas cifras calculadas sobre los pedidos realizados entre las dos fechas (ambas incluidas; si no se indican, los últimos 30 días):

| Dato | Qué significa |
|---|---|
| `totalVentas` | Suma de los totales de los pedidos no cancelados |
| `numeroPedidos` | Número de pedidos no cancelados |
| `ticketMedio` | Importe medio por pedido: `totalVentas` entre `numeroPedidos` (0 si no hay pedidos) |
| `pedidosPorEstado` | Cuántos pedidos hay en cada estado, incluidos los cancelados |
| `ventasPorCategoria` | Para cada categoría, unidades vendidas e importe vendido, sumando las líneas de los pedidos no cancelados |
| `productosMasVendidos` | Los 5 productos con más unidades vendidas en pedidos no cancelados, con sus unidades e importe |

Ejemplo de respuesta:

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

Los cálculos no se hacen recorriendo los pedidos en Java: se hacen en la base de datos con consultas JPQL de agregación (`SUM`, `COUNT`, `GROUP BY`, `ORDER BY`), y los resultados se recogen directamente en DTOs con expresiones `SELECT new ...`. Para comprobar que las cifras son correctas, comparadlas con una consulta SQL hecha a mano en phpMyAdmin.

### 8. Validación de los datos de entrada

Añadid la dependencia Validation (`spring-boot-starter-validation`) para que la API rechace los datos incorrectos antes de que lleguen a los servicios:

- Añadid a todos los DTOs de entrada las anotaciones de Bean Validation que correspondan a cada campo: obligatorio (`@NotBlank` en textos, `@NotNull` en el resto), longitud máxima (`@Size`), números positivos (`@Positive`) o no negativos (`@PositiveOrZero`), formato (`@Pattern`, por ejemplo para el SKU) o correo (`@Email`).
- Cada anotación lleva un mensaje en castellano pensado para el usuario final (por ejemplo, «El precio debe ser mayor que 0»).
- Los controladores validan el cuerpo de las peticiones con `@Valid`. Si los datos no cumplen las restricciones, Spring responde `400` sin ejecutar el método.
- Validad también los parámetros de ruta y de consulta que lo necesiten (por ejemplo, que el umbral de stock bajo no sea negativo), anotando el controlador con `@Validated`.
- Configurad `server.error.include-binding-errors=always` para que la respuesta incluya los errores de cada campo, que la aplicación Vue mostrará junto al formulario.

Bean Validation comprueba el formato de los datos; las reglas de negocio (SKU único, stock suficiente, transiciones de estado...) siguen en los servicios.

### 9. Investigación: importar pedidos desde Google Sheets
{: .investigacion}

> Este apartado no se explica en clase. Es trabajo de investigación y debe documentarse en la memoria.
{: .investigacion}

El cliente prepara su pedido en una hoja de Google Sheets compartida como «Cualquier persona con el enlace» → Lector y envía solo el enlace. La hoja tiene una fila de cabecera con las columnas `sku` y `cantidad` (obligatorias, en cualquier orden) y `comentario` (opcional):

| sku | cantidad | comentario |
|---|---|---|
| NSW-MK8 | 2 | Para el cumpleaños |
| PS5-GOW | 1 | |

| Método | Ruta | Cuerpo | Descripción |
|---|---|---|---|
| `POST` | `/api/carrito/importar/previsualizar` | `{ "url": "https://docs.google.com/spreadsheets/d/.../edit#gid=0" }` | Valida la hoja y devuelve el resultado sin modificar el carrito |
| `POST` | `/api/carrito/importar` | `{ "url": "...", "modo": "SUMAR", "estricto": false }` | Importa en el carrito. `modo`: `SUMAR` o `REEMPLAZAR`. Con `estricto`, si alguna fila falla no se importa nada |

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
5. Validar cada fila (SKU existente y activo, cantidad entera positiva, stock) reutilizando las reglas del carrito. La importación en modo estricto debe ser transaccional (apartado 6).
6. Seguridad (SSRF): la URL que se descarga la construye vuestro código a partir del identificador, nunca se descarga la URL recibida. Limitad el tamaño y el número de filas.

Cread hojas de prueba (correcta, con errores, sin la columna `cantidad` y privada) y enlazadlas en el `README.md` y en Postman.

### 10. Investigación: documentación con OpenAPI y pruebas en Postman
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

- La aplicación se conecta a la base de datos MariaDB de XAMPP y los servicios ya no guardan datos en memoria.
- Entidades con Lombok (sin `@Data`) y todas las relaciones de la tabla.
- Las entidades no se exponen en la API.
- Las consultas mínimas del apartado 5 están implementadas y los filtros se resuelven en la base de datos, no en memoria.
- Operaciones críticas transaccionales, con el *rollback* demostrado.
- Estadísticas calculadas con consultas de agregación.
- Todos los DTOs de entrada validados con Bean Validation y mensajes en castellano.
- Importación desde Google Sheets con previsualización, modos, errores por fila y medidas contra SSRF.
- Swagger UI completo y colección de Postman con pruebas que se ejecuta entera sin fallos.
- Los datos se conservan al reiniciar y la colección de Postman de la Versión 1 sigue funcionando.
- Etiqueta `v2` en `main`.

## Reparto en la pareja

| Persona A | Persona B |
|---|---|
| Base de datos y configuración | Diagrama entidad-relación |
| Entidades y repositorios de catálogo y usuarios | Entidades y repositorios de carrito y pedidos |
| Consultas del catálogo | Consultas de pedidos y usuarios |
| Estadísticas de ventas | Transacciones y demostración del *rollback* |
| Validación de los DTOs de catálogo y usuarios | Validación de los DTOs de carrito, pedidos e importación |
| Importación: enlace, descarga y SSRF | Importación: CSV, validación por fila y modos |
| Swagger | Pruebas automáticas de Postman |

## Criterios de finalización

- Toda la API funciona igual que en la Versión 1, ahora con base de datos, y los datos sobreviven a un reinicio.
- `GET /api/productos?categoriaId=2&precioMax=60&orden=PRECIO&direccion=ASC` devuelve los productos correctos, filtrados por la base de datos.
- Un pedido que falla a mitad no deja cambios en la base de datos.
- Los datos no válidos se rechazan con `400` indicando qué campos fallan y por qué.
- Las cifras de las estadísticas coinciden con una consulta SQL hecha a mano.
- Una hoja de Google Sheets correcta se convierte en el carrito; una con errores informa de cada fila; una privada o un enlace que no es de Google Sheets se rechazan.
- Toda la API se puede probar desde Swagger UI y el *Collection Runner* ejecuta la colección sin fallos.

## Entrega

Incluid en la memoria:

- Diagrama entidad-relación y explicación de cada relación (tipo, lado propietario, `cascade`, `fetch`).
- Anotaciones de Lombok utilizadas y por qué no se usa `@Data`.
- Las consultas implementadas y el SQL que generan.
- Capturas de la demostración del *rollback* y de las estadísticas comprobadas con SQL.
- Capturas de las tablas con datos.
- Capturas de errores de validación con los mensajes de cada campo.
- Investigación sobre la importación: exportación CSV de Google Sheets, `RestClient`, librería CSV, qué es SSRF y cómo se evita, y capturas con cada hoja de prueba.
- Investigación sobre OpenAPI y Postman: capturas de Swagger UI y del *Collection Runner*.

Consultad los requisitos comunes de entrega en el [enunciado general](../enunciado_proyecto.md#contenido-de-cada-entrega).

---

[← Versión 1](version_1.md) · [Versión 3 →](version_3.md)
