---
title: Aplicaciones Spring Boot
description: "<strong>Módulo: </strong>Programación Web Avanzada <br> <strong>Profesor:</strong> Matías Montávez Sánchez"
---

[⌂ Volver al inicio](index.md)

## Índice

1. [Parámetros de ruta con PathVariable](#1-parámetros-de-ruta-con-pathvariable)
2. [Actualizar y eliminar recursos](#2-actualizar-y-eliminar-recursos)
3. [Servicios y organización por capas](#3-servicios-y-organización-por-capas)
4. [Inyección de dependencias](#4-inyección-de-dependencias)
5. [HTTP y ResponseEntity](#5-http-y-responseentity)
6. [DTOs: datos de entrada y salida](#6-dtos-datos-de-entrada-y-salida)
7. [Inicio de sesión con Firebase Authentication](#7-inicio-de-sesión-con-firebase-authentication)

{% comment %}
8. [Práctica 1: reservas de una sala](#8-práctica-1-reservas-de-una-sala)
9. [Práctica 2: estrategias de facturación](#9-práctica-2-estrategias-de-facturación)
10. [Práctica 3: telemetría de un rover](#10-práctica-3-telemetría-de-un-rover)
11. [Actividades de ampliación](#actividades-de-ampliación)
{% endcomment %}


## 1. Parámetros de ruta con PathVariable

`@PathVariable` enlaza una parte variable de la ruta con un parámetro Java. Se utiliza para dirigirse a un recurso concreto:

```java
@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> buscar(@PathVariable("id") long id) {
        return reservaService.buscar(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
```

La ruta `GET /reservas/12` invoca el método con `id = 12`. El nombre del parámetro entre llaves (`{id}`) debe coincidir con el valor de `@PathVariable`, o puede omitirse el nombre si el compilador conserva los nombres de parámetros.

No hay que confundirlo con `@RequestParam`: `GET /reservas?nombre=Lucia` usa un parámetro de consulta, mientras que `GET /reservas/12` identifica el recurso en el propio camino de la URL.

## 2. Actualizar y eliminar recursos

### PUT

`PUT` actualiza o reemplaza un recurso identificado por su URL. Habitualmente, el cliente envía la representación completa que quiere guardar. Es **idempotente**: repetir la misma petición deja el recurso en el mismo estado final.

```java
@PutMapping("/{id}")
public ResponseEntity<ReservaDto> actualizar(
        @PathVariable long id,
        @RequestBody ReservaActualizarDto entrada) {
    return reservaService.actualizar(id, entrada)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}
```

Si el diseño solo permite cambiar algunos campos, debe definirse claramente qué campos se actualizan; no se deben borrar accidentalmente los que el cliente no envíe.

### DELETE

`DELETE` elimina el recurso identificado por la ruta. También es idempotente respecto al estado final del servidor: después de borrarlo, el recurso ya no existe. Una API puede contestar `404` al intentar borrarlo otra vez, aunque el estado no haya cambiado.

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> eliminar(@PathVariable long id) {
    boolean eliminado = reservaService.eliminar(id);
    return eliminado
            ? ResponseEntity.noContent().build()
            : ResponseEntity.notFound().build();
}
```

`204 No Content` es una respuesta adecuada cuando el borrado se completa y no hace falta devolver un cuerpo.

## 3. Servicios y organización por capas

Una organización habitual separa responsabilidades:

- **Controller (`@RestController`):** recibe las peticiones HTTP, enlaza parámetros y DTOs, y elige el estado HTTP.
- **Service (`@Service`):** aplica las reglas de negocio y coordina operaciones.
- **Repository (`@Repository`):** consulta o guarda datos; puede apoyarse en Spring Data. En una práctica sin base de datos, el servicio puede utilizar una colección en memoria.
- **DTO:** define los datos que entran y salen de la API.


`@Service` registra una clase como componente de la capa de negocio. El controlador delega en el servicio, en lugar de contener reglas de reservas, descuentos o movimiento:

```java
@Service
public class InventarioService {
    public boolean hayUnidades(int disponibles, int solicitadas) {
        return solicitadas > 0 && disponibles >= solicitadas;
    }
}
```

`@Component` es una anotación de propósito general para registrar un bean. `@Service`, `@Repository` y `@RestController` son anotaciones especializadas que expresan el papel del componente.

## 4. Inyección de dependencias

Spring crea y administra los objetos de la aplicación, llamados *beans*. En vez de que un controlador construya sus servicios con `new`, Spring puede inyectar las dependencias necesarias. Esto reduce el acoplamiento y hace más sencillo probar cada parte.

### Inyección mediante constructor con `@Autowired`

La inyección por constructor deja visible qué dependencias necesita una clase y permite mantenerlas como campos `final`. Spring moderno suele detectar automáticamente un único constructor, pero `@Autowired` puede escribirse de forma explícita:

```java
@Service
public class ReservaService {
    private final ReservaRepository reservaRepository;

    @Autowired
    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }
}
```

El controlador recibe el servicio del mismo modo:

```java
@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    @Autowired
    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }
}
```

La inyección por campo también existe, pero el constructor suele ser preferible: hace explícitas las dependencias, facilita las pruebas y permite declarar los campos como inmutables.

## 5. HTTP y ResponseEntity

Una aplicación web intercambia mensajes mediante el protocolo HTTP. El navegador u otro cliente envía una **petición** al servidor Spring Boot, y el servidor devuelve una **respuesta**. Cada operación se dirige a una URL y utiliza un método HTTP que expresa la intención: consultar, crear, reemplazar o borrar un recurso.

Una respuesta HTTP tiene tres componentes:

1. **Código de estado:** informa del resultado general. Por ejemplo, `200 OK`, `404 Not Found` o `500 Internal Server Error`.
2. **Cabeceras:** metadatos de la respuesta, como `Content-Type: application/json`.
3. **Cuerpo:** los datos devueltos, normalmente JSON en una API REST, o HTML en una aplicación con vistas.

En Chrome se puede inspeccionar el tráfico desde las herramientas de desarrollador: abre **Network (Red)**, genera una petición y selecciona la fila correspondiente para consultar estado, cabeceras y cuerpo.

### Códigos de estado habituales

| Código | Significado | Uso habitual en una API |
|---|---|---|
| `200 OK` | Operación correcta | Consulta, actualización o resultado con cuerpo |
| `201 Created` | Recurso creado | Alta correcta mediante `POST` |
| `202 Accepted` | Petición aceptada | Trabajo que se procesará más adelante |
| `204 No Content` | Operación correcta sin cuerpo | Borrado correcto |
| `400 Bad Request` | Petición no válida | Datos mal formados o reglas de entrada incumplidas |
| `401 Unauthorized` | No hay autenticación válida | Falta un token o no se puede validar |
| `403 Forbidden` | Identidad reconocida, sin permiso | Acceso denegado |
| `404 Not Found` | Recurso inexistente | No se encuentra el identificador solicitado |
| `409 Conflict` | Conflicto con el estado actual | Horario ocupado o recurso duplicado |
| `410 Gone` | Recurso ya no disponible | Estado terminal, como un rover destruido |
| `500 Internal Server Error` | Error inesperado del servidor | Fallo no previsto durante el procesamiento |

El estado HTTP forma parte del contrato de la API. No conviene responder `200 OK` con un texto que diga que hubo un error: el cliente debe poder interpretar correctamente el resultado sin analizar mensajes.

### ResponseEntity

Spring puede convertir automáticamente un objeto devuelto por un controlador a JSON. Si el método devuelve directamente un objeto, normalmente responderá `200 OK`. Para decidir también el estado y las cabeceras se utiliza `ResponseEntity<T>`.

```java
@GetMapping("/{id}")
public ResponseEntity<ReservaDto> buscar(@PathVariable long id) {
    return reservaService.buscar(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}
```

`ResponseEntity` permite especificar de forma explícita los componentes de la respuesta:

- `ResponseEntity.ok(cuerpo)`: `200 OK` con contenido.
- `ResponseEntity.created(uri).body(cuerpo)`: `201 Created` con el recurso creado y su ubicación.
- `ResponseEntity.accepted().body(cuerpo)`: `202 Accepted`.
- `ResponseEntity.noContent().build()`: `204 No Content`.
- `ResponseEntity.badRequest().body(error)`: `400 Bad Request`.
- `ResponseEntity.notFound().build()`: `404 Not Found`.
- `ResponseEntity.status(codigo).body(cuerpo)`: permite elegir otro código.

Cuando una operación puede devolver cuerpos de clases distintas —por ejemplo, un DTO normal o un DTO de error— se puede declarar `ResponseEntity<?>`. El comodín expresa que el tipo concreto del cuerpo puede variar; no sustituye a un diseño claro de las respuestas.

```java
@PostMapping
public ResponseEntity<?> crear(@RequestBody ReservaCrearDto entrada) {
    int horaFin = entrada.horaInicio() + entrada.duracionHoras();
    if (entrada.horaInicio() < 9
            || entrada.horaInicio() >= 20
            || entrada.duracionHoras() <= 0
            || horaFin > 20) {
        return ResponseEntity.badRequest()
                .body(new ErrorDto("La reserva debe quedar dentro del horario 9-20"));
    }

    ReservaDto creada = reservaService.crear(entrada);
    return ResponseEntity.status(HttpStatus.CREATED).body(creada);
}
```

Para una API con reglas de error comunes se puede centralizar el tratamiento de excepciones con `@RestControllerAdvice`. El ejemplo anterior es breve para mostrar la forma de la respuesta; en una aplicación mayor, la validación debe quedar en una capa coherente y no repetirse en cada controlador.

## 6. DTOs: datos de entrada y salida

Un **DTO** (*Data Transfer Object*) representa los datos que cruzan el límite de la API. Separa el contrato HTTP de las clases internas de negocio o persistencia y permite controlar qué información se expone.

Por ejemplo, un DTO de entrada para reservar una sala no necesita un identificador: ese valor lo asigna el servidor. El DTO de respuesta sí puede incluirlo.

```java
public record ReservaCrearDto(
        String nombre,
        int horaInicio,
        int duracionHoras) {
}

public record ReservaDto(
        long id,
        String nombre,
        int horaInicio,
        int duracionHoras) {
}

public record ErrorDto(String mensaje) {
}
```

Spring utiliza Jackson para convertir JSON en objetos Java anotados con `@RequestBody` y convertir objetos devueltos en JSON. Una petición de ejemplo podría ser:

```http
POST /reservas
Content-Type: application/json

{
  "nombre": "Lucía Martín",
  "horaInicio": 10,
  "duracionHoras": 2
}
```

La respuesta de creación puede contener el identificador generado:

```http
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "nombre": "Lucía Martín",
  "horaInicio": 10,
  "duracionHoras": 2
}
```

Para que los datos inválidos no lleguen a la lógica de negocio, se pueden añadir restricciones de Bean Validation a los DTOs, por ejemplo `@NotBlank`, `@Min` o `@Max`, y validar el cuerpo con `@Valid`.

## 7. Inicio de sesión con Firebase Authentication

Firebase Authentication permite que el cliente inicie sesión mediante los proveedores configurados (correo, Google u otros). El flujo recomendado para una API Spring es:

1. El cliente autentica al usuario mediante el SDK de Firebase.
2. Firebase devuelve un **ID token** firmado y de duración limitada.
3. El cliente envía el token a Spring por HTTPS, normalmente en `Authorization: Bearer <idToken>`.
4. El backend verifica la firma, el emisor, el proyecto destinatario y la caducidad mediante **Firebase Admin SDK**.
5. Solo después de validar el token se utiliza el UID o las reclamaciones verificadas para identificar al usuario y autorizar la operación.

El backend **no** debe recibir ni almacenar la contraseña de Firebase. Un UID enviado en el cuerpo de la petición tampoco demuestra la identidad del usuario: la identidad se obtiene de las reclamaciones del token verificado.

### DTO y servicio de verificación

Para una demostración didáctica se puede enviar el ID token en el cuerpo. En una API protegida de producción es preferible validarlo en un filtro de seguridad desde la cabecera `Authorization`.

```java
public record LoginFirebaseDto(String idToken) {
}

public record UsuarioAutenticadoDto(String uid, String email) {
}
```

El servicio encapsula la llamada al SDK:

```java
@Service
public class FirebaseAuthService {
    private final FirebaseAuth firebaseAuth;

    @Autowired
    public FirebaseAuthService(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public UsuarioAutenticadoDto verificar(String idToken)
            throws FirebaseAuthException {
        FirebaseToken token = firebaseAuth.verifyIdToken(idToken);
        return new UsuarioAutenticadoDto(
                token.getUid(),
                token.getEmail());
    }
}
```

El bean `FirebaseAuth` debe inicializarse una sola vez al arrancar Spring y configurarse con credenciales de servidor. En local se pueden usar las credenciales de aplicación predeterminadas; en despliegues, utiliza la identidad de servicio del entorno. No incluyas archivos JSON de cuentas de servicio, claves privadas ni tokens en el repositorio.

### Endpoint de ejemplo

```java
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final FirebaseAuthService firebaseAuthService;

    @Autowired
    public AuthController(FirebaseAuthService firebaseAuthService) {
        this.firebaseAuthService = firebaseAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioAutenticadoDto> login(
            @RequestBody LoginFirebaseDto entrada) throws FirebaseAuthException {
        return ResponseEntity.ok(
                firebaseAuthService.verificar(entrada.idToken()));
    }
}
```

Un manejador específico convierte los errores de verificación en `401 Unauthorized`, sin exponer detalles internos del SDK:

```java
@RestControllerAdvice
public class AuthExceptionHandler {
    @ExceptionHandler(FirebaseAuthException.class)
    public ResponseEntity<ErrorDto> tokenInvalido(FirebaseAuthException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorDto("El token de Firebase no es válido"));
    }
}
```

El cliente obtiene el token con Firebase y lo envía a `POST /auth/login`. Si no se puede verificar, la aplicación responde con `401 Unauthorized`, no con un usuario vacío ni con `200 OK`. En una aplicación real también se protegen las rutas con Spring Security.

La verificación del token autentica al usuario, pero no concede automáticamente todos los permisos. La autorización (por ejemplo, roles o acceso a un recurso propio) se decide en el backend utilizando reclamaciones verificadas y reglas de negocio. Usa siempre HTTPS y no registres tokens completos.

{% comment %}

## 8. Práctica 1: reservas de una sala

### Objetivo

Construye una API REST para administrar las reservas de una única sala durante el día actual. Por ahora los datos pueden permanecer en memoria; no hace falta conectar una base de datos.

### Modelo y reglas

Cada reserva incluye un identificador generado por el sistema, el nombre de quien reserva, una hora de inicio expresada como hora entera (por ejemplo, `9`, `14` o `18`) y una duración en horas.

- La sala está disponible entre las 09:00 y las 20:00.
- No se acepta una reserva que empiece antes de las 9 ni una cuyo final supere las 20.
- No se permiten intervalos que se solapen con otra reserva.
- Si una reserva empieza exactamente cuando termina otra, no hay solapamiento.

Para intervalos semiabiertos `[inicio, fin)`, dos reservas se solapan cuando:

```text
nuevoInicio < reservaExistenteFin
y
nuevoFin > reservaExistenteInicio
```

### API solicitada

| Operación | Resultado esperado |
|---|---|
| `POST /reservas` | Crea una reserva; devuelve `201 Created` y el DTO creado |
| `GET /reservas` | Devuelve la lista; `200 OK` |
| `GET /reservas/{id}` | Devuelve el recurso o `404 Not Found` |
| `DELETE /reservas/{id}` | Elimina; `204 No Content` si existe o `404 Not Found` |
| `GET /reservas/ocupacion` | Informa de horas ocupadas y libres dentro del horario de apertura |

Al crear una reserva, responde `400 Bad Request` si se sale del horario y `409 Conflict` si el intervalo se solapa. Los errores deben explicar el motivo mediante un DTO, por ejemplo `{"mensaje":"La sala ya está ocupada en ese horario"}`.

Las horas disponibles se calculan sobre el intervalo de apertura (11 horas). Si se registran reservas de 9 a 11 y de 14 a 16, hay 4 horas ocupadas y 7 libres.

**Ampliación:** añade `PUT /reservas/{id}` para modificar una reserva y vuelve a comprobar las reglas de horario y solapamiento sin considerar la reserva que se está editando como conflicto consigo misma.

## 9. Práctica 2: estrategias de facturación

### Objetivo

Crea `POST /compra` para recibir un carrito JSON, calcular su factura y devolverla como JSON. No se persiste el carrito. El periodo promocional selecciona una política de cálculo.

Diseña al menos un `CarritoDto`, un DTO de línea de producto y un `FacturaDto`. La factura debe incluir los importes necesarios para justificar el total, como subtotal, descuentos, coste de envío y total final. Define una interfaz de estrategia, por ejemplo `PromocionStrategy`, con un código de periodo y una operación que calcule la factura a partir del carrito.

### Periodos y reglas

- **STANDARD:** sin descuento por categoría. Cuando el subtotal supera los 1.000 €, se aplica un 5 % al total de productos. El envío cuesta 10 €, o 20 € si el peso total supera los 20 kg.
- **SUMMER:** 20 % de descuento en ropa y 15 % en juguetes. Si hay al menos dos unidades de comida, la unidad de menor valor se ofrece gratis. El envío cuesta 5 €.
- **BLACK_FRIDAY:** por cada pareja de unidades de un mismo producto, una se cobra a precio completo y la otra tiene un 50 % de descuento; en grupos impares, la unidad sobrante se cobra completa. Si hay más de cinco productos distintos, aplica un 10 % adicional al total de productos. El envío es gratuito.

Define y documenta claramente cómo se combinan los descuentos, el envío y el redondeo monetario; utiliza `BigDecimal` para representar importes en vez de `float` o `double`.

### Selección de la estrategia

El controlador no debe contener un `if/else` o `switch` para escoger las reglas. Cada estrategia se registra como bean Spring y el servicio recibe todas las implementaciones por inyección. El servicio localiza la que corresponde al periodo y ejecuta su cálculo.

Una opción es asociar cada estrategia a un código (`STANDARD`, `SUMMER`, `BLACK_FRIDAY`) y construir un mapa al inicializar el servicio. Si el código no existe, la API debe devolver `400 Bad Request` con un error claro.

```java
public interface PromocionStrategy {
    String codigo();
    FacturaDto calcular(CarritoDto carrito);
}
```

El endpoint puede recibir el periodo como parámetro de consulta:

```text
POST /compra?periodo=SUMMER
POST /compra?periodo=BLACK_FRIDAY
```

El cuerpo JSON representa el carrito. El controlador delega el cálculo en un servicio, que selecciona e invoca la estrategia. Así, añadir un nuevo periodo no requiere añadir ramas condicionales al controlador.

## 10. Práctica 3: telemetría de un rover

### Objetivo y configuración

Implementa una API para desplegar un rover, recibir órdenes de movimiento y consultar su estado. En esta práctica no se usa una base de datos externa: cada operador mantiene el estado temporal en su sesión HTTP.

Al iniciar la aplicación, carga `config.json` desde `src/main/resources`. La configuración incluye:

- `tamanoMapa`: lado del terreno cuadrado, con coordenadas válidas entre 0 y 50.
- `zonasRestringidas`: posiciones de cráteres en las que no puede entrar el rover.
- `tokenSeguridad`: valor que debe presentar una orden de movimiento.
- Batería inicial y coste de cada movimiento, según los campos que se definan para la práctica.

### Operaciones y comprobaciones

**Despliegue:** reinicia la sesión del operador, coloca el rover en `(0, 0)` y asigna la batería inicial. Devuelve `200 OK` y el estado inicial.

**Movimiento:** comprueba las condiciones en este orden:

1. Si no existe un despliegue en la sesión, devuelve `401 Unauthorized`.
2. Si el token de seguridad recibido no coincide con la configuración, devuelve `403 Forbidden`.
3. Si el rover ya está destruido, devuelve `410 Gone`.
4. Si el destino sale de los límites del mapa, devuelve `400 Bad Request`.
5. Si el destino es un cráter, marca el rover como destruido, fija la batería a cero y devuelve `409 Conflict`.
6. Si la batería no cubre el coste del movimiento, no cambia la posición y devuelve `406 Not Acceptable`.
7. En otro caso, descuenta la batería, actualiza la posición y devuelve `200 OK` con el estado actualizado.

**Consulta:** devuelve `200 OK` con la telemetría actual o `404 Not Found` si todavía no hay rover desplegado en esa sesión.

El DTO de telemetría puede incluir posición, batería y estado de destrucción. El servicio debe aplicar las reglas de movimiento; el controlador se limita a traducir la petición en una llamada y a elegir la respuesta HTTP correspondiente.

## Actividades de ampliación

1. Usa Postman o el cliente HTTP del IDE para probar cada endpoint. Registra el método, URL, cuerpo, estado HTTP y respuesta.
2. Añade validación de entrada a los DTOs y respuestas consistentes para los errores.
3. Implementa un manejador común con `@RestControllerAdvice`.
4. Protege una ruta con Firebase Authentication y comprueba que las peticiones con token inválido reciben `401`.
5. Escribe pruebas unitarias para servicios y estrategias, y pruebas de integración para los estados HTTP.
6. Explica qué datos de las prácticas se perderían al reiniciar la aplicación y qué cambiaría al sustituir las colecciones en memoria por un repositorio persistente.
{% endcomment %}
