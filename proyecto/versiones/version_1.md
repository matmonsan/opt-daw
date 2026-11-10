---
title: Versión 1
description: "<strong>Módulo:</strong> Programación Web Avanzada <br> <strong>Profesor:</strong> Matías Montávez Sánchez"
---
[⌂ Volver al enunciado del proyecto](../enunciado_proyecto.md)

# **FECHA DE ENTREGA:** 29/11/2026

# Versión 1 — API REST completa sin base de datos

## Objetivo

Construir la estructura completa del backend de la tienda con Spring Boot: diseño, repositorio con una rama por persona, catálogo, búsqueda, excepciones, autenticación, carrito y pedidos.

En esta versión no hay base de datos: los datos se guardan en memoria y se pierden al reiniciar la aplicación. La persistencia llegará en la Versión 2.

## Planificación orientativa

| Semana | Trabajo |
|---|---|
| 09/11 - 15/11 | Apartados 1 a 4: diseño, repositorio y ramas, proyecto base y catálogo |
| 16/11 - 22/11 | Apartados 5 a 7: búsqueda y filtros, excepciones, autenticación |
| 23/11 - 29/11 | Apartado 8: carrito y pedidos, y revisión final con la colección de Postman |

---

## Enunciado

### 1. Diseño de la tienda

Elegid la temática y el nombre de la tienda y describidlos en el `README.md`, junto con los nombres de la pareja, la rama de cada persona y cómo se llamarán en vuestro proyecto las clases genéricas (`Producto` → `Videojuego`, `Categoria` → `Genero`...).

El modelo de la tienda tendrá, como mínimo, estas clases:

| Clase | Atributos mínimos |
|---|---|
| `Categoria` | `id`, `nombre`, `descripcion` |
| `Producto` | `id`, `sku` (código único), `nombre`, `descripcion`, `precio`, `stock`, `imagenUrl`, `activo`, `destacado`, `fechaAlta`, `categoria`, dos atributos propios de la temática |
| `Usuario` | `id`, `uid` (identificador de Firebase o Supabase; con Spring Security, la contraseña cifrada), `email`, `nombre`, `direccionEnvio`, `rol`, `fechaAlta` |
| `Carrito` y `LineaCarrito` | `usuario`, `lineas` / `producto`, `cantidad` |
| `Pedido` | `id`, `codigo` (legible, por ejemplo `PED-2026-0001`), `usuario`, `fecha`, `estado`, `direccionEnvio`, `subtotal`, `gastosEnvio`, `total`, `lineas` |
| `LineaPedido` | `producto`, `nombreProducto`, `precioUnitario`, `cantidad`, `subtotal` |
| `Rol` y `EstadoPedido` | enumerados: `CLIENTE`, `ADMIN` / `PENDIENTE`, `PAGADO`, `ENVIADO`, `ENTREGADO`, `CANCELADO` |

- Los importes se representan con `BigDecimal`, nunca con `double`.
- `LineaPedido` guarda una copia del nombre y del precio del producto: si mañana cambia el precio, los pedidos antiguos no cambian.
- Un producto no se borra: se marca como `activo = false` para no romper los pedidos que lo contienen.

Cread también `docs/api.md` con la tabla de todos los *endpoints* (método, ruta, rol, DTO de entrada, DTO de salida y códigos de respuesta) y mantenedlo actualizado.

### 2. Investigación: repositorio y trabajo con ramas
{: .investigacion}

> Este apartado no se explica en clase. Es trabajo de investigación y debe documentarse en la memoria.
{: .investigacion}

Cada persona de la pareja trabajará en su propia rama y nunca directamente en la rama principal:

<img src="../../assets/img/proyecto/ramas-git.svg" alt="Flujo de ramas de la pareja" width="100%">

1. Una persona crea el repositorio e invita a la otra y al profesor.
2. Se crean `develop` y una rama con el nombre de cada persona. Cada persona hace *commits* solo en su rama.
3. Al terminar una tarea, se abre una *Pull Request* hacia `develop`, que la otra persona revisa y aprueba antes de fusionarla.
4. Cada persona actualiza su rama con los cambios de `develop` antes de seguir trabajando (`merge` o `rebase`, justificando la elección).
5. Al terminar la versión, se fusiona `develop` en `main` y se crea la etiqueta `v1`.

Investigad y aplicad también: reglas de protección de ramas (no se permite `push` directo a `main` ni a `develop` y las *Pull Requests* necesitan una aprobación), la resolución de un conflicto provocado a propósito, un `.gitignore` adecuado y una convención de mensajes de *commit* (por ejemplo, *Conventional Commits*).

### 3. Proyecto base y organización por capas

Cread el proyecto en `backend/` con [Spring Initializr](https://start.spring.io/): Maven, Java 21 y la dependencia Spring Web. Organizad los paquetes así:

<img src="../../assets/img/proyecto/paquetes-backend.svg" alt="Paquetes del proyecto Spring Boot" width="100%">

- Los controladores solo reciben la petición, llaman al servicio y devuelven un `ResponseEntity` con el código adecuado.
- Los servicios contienen la lógica de negocio y los datos en memoria, trabajan con DTOs y no conocen `ResponseEntity`.
- Inyección de dependencias por constructor.
- En esta versión no hay capa de repositorio: se incorporará en la Versión 2 junto con la base de datos.
- Las rutas de administración empiezan por `/api/admin`, lo que facilitará protegerlas.

Material de referencia: [apartados 1 a 4 de Aplicaciones Spring Boot](../../aplicaciones.md#1-http-y-apis-rest).

### 4. Catálogo

Los modelos y los DTOs son clases Java con atributos privados, constructores, *getters* y *setters*. DTOs mínimos: `CategoriaDto`, `CategoriaCrearDto`, `ProductoResumenDto` (para listados), `ProductoDetalleDto`, `ProductoCrearDto` y `StockDto`. El cliente nunca envía `id`, `activo` ni `fechaAlta`: los asigna el servidor. La conversión entre el modelo y los DTOs se hace en los servicios.

```java
public class ProductoCrearDto {

    private String sku;
    private String nombre;
    private BigDecimal precio;
    private Integer stock;
    private Long categoriaId;

    // descripcion, imagenUrl, destacado y atributos propios de la temática...

    public ProductoCrearDto() {
    }

    // getters y setters...
}
```

El constructor sin argumentos y los *setters* son necesarios para que Jackson pueda construir el objeto a partir del JSON de la petición. En esta versión los DTOs no llevan anotaciones de validación: la validación de los datos de entrada con Bean Validation se incorporará en la Versión 2.

Datos en memoria: los servicios guardan los datos en memoria, siguiendo los ejemplos vistos en clase.

Cuidado con las dependencias circulares: si `ProductoService` recibe `CategoriaService` y `CategoriaService` recibe `ProductoService` (por ejemplo, para comprobar si una categoría tiene productos antes de borrarla), Spring no podrá arrancar la aplicación. Decidid qué servicio depende de cuál y explicad en la memoria cómo lo habéis resuelto.

Datos de ejemplo: al arrancar, los servicios cargan al menos 3 categorías y 12 productos con imágenes reales, incluyendo alguno sin stock, alguno retirado y alguno destacado.

Reglas: SKU único (se guarda en mayúsculas); no se puede usar una categoría inexistente; no puede haber dos categorías con el mismo nombre; no se puede eliminar una categoría con productos; eliminar un producto lo marca como retirado; el stock nunca queda en negativo; los listados públicos solo muestran productos activos.

Categorías:

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| `GET` | `/api/categorias` | Listar categorías | `200` |
| `GET` | `/api/categorias/{id}` | Consultar una categoría | `200`, `404` |
| `GET` | `/api/admin/categorias` | Listar categorías (administración) | `200` |
| `POST` | `/api/admin/categorias` | Crear una categoría | `201`, `409` |
| `PUT` | `/api/admin/categorias/{id}` | Modificar una categoría | `200`, `404`, `409` |
| `DELETE` | `/api/admin/categorias/{id}` | Eliminar una categoría sin productos | `204`, `404`, `409` |

Productos:

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| `GET` | `/api/productos/{id}` | Detalle de un producto activo | `200`, `404` |
| `GET` | `/api/productos/destacados` | Productos activos destacados | `200` |
| `GET` | `/api/admin/productos` | Listar todos los productos, incluidos los retirados | `200` |
| `POST` | `/api/admin/productos` | Crear un producto | `201`, `400`, `409` |
| `PUT` | `/api/admin/productos/{id}` | Modificar un producto | `200`, `400`, `404`, `409` |
| `DELETE` | `/api/admin/productos/{id}` | Retirar un producto del catálogo | `204`, `404` |
| `PUT` | `/api/admin/productos/{id}/stock` | Sumar o restar stock | `200`, `404`, `409` |
| `PUT` | `/api/admin/productos/{id}/reactivar` | Volver a activar un producto retirado | `200`, `404` |

### 5. Búsqueda, filtros y ordenación

`GET /api/productos` admite estos parámetros, opcionales y combinables:

| Parámetro | Ejemplo | Efecto |
|---|---|---|
| `categoriaId` | `?categoriaId=2` | Productos de esa categoría |
| `q` | `?q=zelda` | Texto en el nombre o la descripción, sin distinguir mayúsculas ni tildes |
| `precioMin`, `precioMax` | `?precioMin=10&precioMax=60` | Rango de precio (`400` si el mínimo supera al máximo) |
| `soloDisponibles` | `?soloDisponibles=true` | Solo productos con stock |
| `orden`, `direccion` | `?orden=PRECIO&direccion=DESC` | Enumerados `NOMBRE`, `PRECIO`, `NOVEDAD` y `ASC`, `DESC` |
| Uno propio | `?plataforma=SWITCH` | Filtro por un atributo de vuestra temática |

Además: `GET /api/categorias/{id}/productos`, `GET /api/productos/sku/{sku}` y `GET /api/admin/productos/stock-bajo?umbral=5`.

- Agrupad los filtros en una clase `ProductoFiltroDto` y aplicadlos en el servicio con *streams* y `Comparator`.
- Comprobad que `/api/productos/destacados` no se confunde con `/api/productos/{id}`.

Material de referencia: [apartado 5 de los apuntes](../../aplicaciones.md#5-parámetros-de-ruta-y-de-consulta).

### 6. Excepciones

- Cread excepciones propias. Los servicios las lanzan y los controladores no tienen `try/catch`.
- Las respuestas de error no muestran trazas ni información interna.
- Por defecto, Spring Boot no incluye en la respuesta el mensaje de las excepciones. Como la aplicación Vue de la Parte 2 tendrá que mostrarlo al usuario, configurad `server.error.include-message=always`. Por eso los mensajes de vuestras excepciones deben estar pensados para el usuario final y no contener información interna.

Material de referencia: [apartado 7 de los apuntes](../../aplicaciones.md#excepciones) (excepciones).

### 7. Investigación: autenticación y roles
{: .investigacion}

> Este apartado no se explica en clase. Es trabajo de investigación y debe documentarse en la memoria.
{: .investigacion}

Elegid una opción y justificadla:

- **Firebase Authentication:** el cliente envía el ID token en `Authorization: Bearer <token>` y el backend lo verifica con Firebase Admin SDK. Punto de partida: [apartado 8 de los apuntes](../../aplicaciones.md#8-inicio-de-sesión-con-firebase-authentication), llevando la verificación a un filtro que proteja todas las rutas.
- **Supabase Auth:** el backend verifica la firma, la caducidad y el emisor del *access token* (JWT) de Supabase.
- **Spring Security:** usuarios propios con contraseña cifrada (`BCryptPasswordEncoder`), `POST /api/auth/registro` y `POST /api/auth/login` (únicas rutas públicas) que devuelven un JWT, y un filtro que lo valida.

Requisitos:

- Sin token, o con un token no válido o caducado: `401`. Un cliente en cualquier ruta `/api/admin/`: `403`.
- La primera vez que un usuario accede se crea su `Usuario` con rol `CLIENTE` (con Spring Security, al registrarse). Los administradores se definen en `application.properties` (`tienda.admins=admin@tienda.com`).
- La identidad se obtiene siempre del token verificado, encapsulada en un `UsuarioActualService` que usará el resto del código.
- Dos usuarios de prueba (cliente y administrador) indicados en el `README.md`; la colección de Postman obtiene el token con una petición de *login* y lo guarda en una variable `{{token}}`.
- Las credenciales privadas no se suben al repositorio; se incluye un `application-example.properties`.

Usuarios:

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/usuarios/me` | Datos del usuario autenticado y su rol |
| `PUT` | `/api/usuarios/me` | Modificar el nombre y la dirección de envío |
| `GET` | `/api/admin/usuarios` | Listar usuarios |
| `PUT` | `/api/admin/usuarios/{id}/rol` | Cambiar el rol de un usuario |

### 8. Carrito y pedidos

Carrito (siempre el del usuario autenticado; las rutas no llevan id de usuario):

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/api/carrito` | Ver el carrito: líneas, número de artículos, subtotal, gastos de envío, total, importe que falta para el envío gratuito y `avisos`, todo calculado por el servidor |
| `POST` | `/api/carrito/lineas` | Añadir un producto: `{ "productoId": 7, "cantidad": 2 }`; si ya existe, suma la cantidad |
| `PUT` | `/api/carrito/lineas/{productoId}` | Cambiar la cantidad de una línea |
| `DELETE` | `/api/carrito/lineas/{productoId}` | Quitar una línea |
| `DELETE` | `/api/carrito` | Vaciar el carrito |

- La cantidad mínima de una línea es 1 (`400`). No se pueden añadir productos retirados ni más unidades que el stock (`409`) ni superar `tienda.carrito.max-unidades-por-producto`.
- Si un producto del carrito se retira o baja su stock, el carrito lo indica en `avisos` y no se puede confirmar el pedido.
- Gastos de envío configurables en `application.properties` (`tienda.envio.coste` y `tienda.envio.gratis-desde`) y leídos con `@Value`.

Pedidos:

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/api/pedidos` | Confirmar el carrito (dirección del cuerpo o del perfil) |
| `GET` | `/api/pedidos?estado=` | Mis pedidos, con filtro opcional por estado |
| `GET` | `/api/pedidos/{id}` | Detalle de uno de mis pedidos (`404` si es de otro usuario) |
| `PUT` | `/api/pedidos/{id}/cancelar` | Cancelar uno de mis pedidos |
| `GET` | `/api/admin/pedidos?estado=&desde=&hasta=&email=` | Todos los pedidos, filtrando por estado, fechas y correo del cliente |
| `GET` | `/api/admin/pedidos/{id}` | Detalle de cualquier pedido |
| `PUT` | `/api/admin/pedidos/{id}/estado` | Cambiar el estado de un pedido |

Al confirmar: se comprueba que el carrito no esté vacío y que todas las líneas tienen stock (si alguna falla, se responde `409` indicando qué productos no tienen stock suficiente y no se modifica nada), se crea el pedido `PENDIENTE` con la copia de nombres y precios, se descuenta el stock y se vacía el carrito. Al cancelar, se repone el stock. Las transiciones de estado se definen en el propio enumerado:

```java
public enum EstadoPedido {
    PENDIENTE, PAGADO, ENVIADO, ENTREGADO, CANCELADO;

    public boolean puedeCambiarA(EstadoPedido nuevo) {
        return switch (this) {
            case PENDIENTE -> nuevo == PAGADO || nuevo == CANCELADO;
            case PAGADO -> nuevo == ENVIADO || nuevo == CANCELADO;
            case ENVIADO -> nuevo == ENTREGADO;
            case ENTREGADO, CANCELADO -> false;
        };
    }
}
```

El cliente solo puede cancelar; el resto de cambios los hace el administrador. Una transición no válida responde `409`.

---

## Requisitos

- Repositorio con la estructura indicada, ramas protegidas, una rama por persona y *Pull Requests* revisadas, con un conflicto resuelto.
- Organización por capas, DTOs en la API e inyección por constructor.
- Todos los *endpoints* indicados con los códigos HTTP correctos y las reglas de negocio implementadas.
- Excepciones propias lanzadas desde los servicios.
- Ninguna ruta responde sin token válido (salvo las públicas indicadas) y las de administración devuelven `403` a los clientes.
- Colección de Postman con todas las peticiones, incluidas las que deben fallar.
- Ningún secreto en el repositorio. Etiqueta `v1` en `main`.

## Reparto en la pareja

| Persona A | Persona B |
|---|---|
| Repositorio, ramas, protección y proyecto base | Modelo y contrato de la API |
| Categorías y búsqueda/filtros | Productos, ordenación y datos de ejemplo |
| Autenticación: filtro, `401`/`403` y secretos | Usuarios, roles, `UsuarioActualService` y `/api/usuarios/me` |
| Carrito y gastos de envío | Pedidos y ciclo de vida |

## Criterios de finalización

- El proyecto arranca siguiendo el `README.md` y se puede probar desde Postman.
- Flujo completo demostrable: iniciar sesión → consultar el catálogo con filtros → añadir al carrito → confirmar el pedido → el stock baja → el administrador lo marca como `PAGADO` → el cliente lo cancela → el stock se repone.
- Las operaciones que incumplen las reglas de negocio y los accesos sin permiso se rechazan con el código adecuado.
- El historial de Git muestra el trabajo de las dos personas.

## Entrega

Incluid en la memoria:

- Temática y tabla de *endpoints*.
- Capturas de las pruebas más relevantes: flujo de compra, filtros, `400`, `401`, `403`, `404` y `409`.
- Investigación sobre Git: flujo de ramas, reglas de protección, una *Pull Request* revisada y el conflicto resuelto.
- Investigación sobre autenticación: opción elegida, flujo de una petición autenticada, qué es un JWT y cómo se obtiene el token para las pruebas.

Consultad los requisitos comunes de entrega en el [enunciado general](../enunciado_proyecto.md#contenido-de-cada-entrega).

---

[Versión 2 →](version_2.md)
