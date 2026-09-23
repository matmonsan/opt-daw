---
title: "Arquitecturas y tecnologías en la programación Web"
description: "<strong>Profesor:</strong> Matías Montávez Sánchez"
---

[⌂ Volver al inicio](index.md)

## Índice

1. [Entorno de trabajo](#1-entorno-de-trabajo)
   - [1.1. Java - JDK](#11-java---jdk)
   - [1.2. IntelliJ IDEA](#12-intellij-idea)
   - [1.3. Postman](#13-postman)
2. [Spring Boot](#2-spring)
   - [2.1. ¿Qué es?](#21-qué-es-spring-boot)
   - [2.2. Ventajas](#ventajas-de-usar-spring-boot)
   - [2.3. Primer proyecto](#23-primer-proyecto)
3. [Anatomía de una Aplicación Spring Boot](#3-anatomía-de-una-aplicación-spring-boot)
   - [3.1. Ficheros Importantes en un Proyecto Spring Boot](#31-ficheros-importantes)
   - [3.2. Clase principal](#32-clase-principal)
   - [3.3. Archivo application.properties](#33-archivo-applicationproperties)
4. [Web estática vs web dinámica](#4-web-estática-vs-web-dinámica)
   - [4.1. Página web estática](#41-página-web-estática)
   - [4.2. Página web dinámica](#42-página-web-dinámica)
5. [Creación de páginas web con Spring Boot](#5-creación-de-páginas-web-con-spring-boot)
   - [5.1. Anotaciones más comunes](#51-anotaciones-más-comunes)
   - [5.2. Estructura típica por capas](#52-estructura-típica-por-capas)
   - [5.3. Carpeta resources](#53-carpeta-resources)
6. [Creación de Páginas Web Estáticas en Spring Boot](#6-creación-de-páginas-web-estáticas-en-spring-boot)
7. [Creación de Páginas Web Dinámicas en Spring Boot](#7-creación-de-páginas-web-dinámicas-en-spring-boot)
   - [7.1. Introducción a controladores](#71-introducción-a-controladores)
8. [Métodos HTTP en Spring Boot](#8-métodos-http-en-spring-boot)
   - [8.1. Método GET](#81-método-get)
   - [8.2. Método POST](#82-método-post)
9. [JSON](#9-json)
10. [Ejecutando la aplicación](#ejecutando-la-aplicación)
11. [Actividades](#actividades)

## 1. Entorno de trabajo

### Instalaciones necesarias

- **Java JDK:** Descarga e instala la última versión (nosotros utilizaremos Java 21).
- Verifica con: `java -version`
- **IntelliJ IDEA:** Ultimate tiene mejor soporte para Spring, pero con Community también se puede.
- **Postman:** Para probar tus endpoints REST.
- **Base de datos:** MySQL, PostgreSQL o la que prefieras. También puedes empezar con H2 (en memoria).

### 1.1. Java - JDK

<https://www.oracle.com/java/technologies/downloads/>

```text
C:\Users\2DAM>java -version
java version "21.0.8" 2025-07-15 LTS
Java(TM) SE Runtime Environment (build 21.0.8+12-LTS-250)
Java HotSpot(TM) 64-Bit Server VM (build 21.0.8+12-LTS-250, mixed mode, sharing)
```

### 1.2. IntelliJ IDEA

<https://www.jetbrains.com/idea/download/?section=windows>

### 1.3. Postman

Se puede descargar o usar la versión online.

<https://www.postman.com/downloads/>

## 2. Spring

![Spring](/assets/img/spring.png)

Spring es un framework de Java muy popular que facilita el desarrollo de aplicaciones empresariales. Permite construir aplicaciones más modulares, fáciles de probar y mantener.

### Características

- Spring se encarga de crear y gestionar los objetos (beans) en lugar de que tú los instancies manualmente.
- Programación orientada a aspectos, separar preocupaciones transversales.
- Spring MVC.
- Integración.

### 2.1. ¿Qué es Spring Boot?

![¿Qué es Spring Boot?](/assets/img/que-spring-boot.jpg)

Spring Boot es un framework basado en Spring que simplifica la creación de aplicaciones Java al proporcionar configuraciones automáticas y convenciones preestablecidas.

Permite desarrollar aplicaciones independientes y listas para producción con **mínima configuración**.

#### Ventajas de Usar Spring Boot

- **Configuración Automática (Auto-Configuration):** Spring Boot configura automáticamente los componentes según las dependencias presentes en el proyecto.
- **Servidor Embebido:** Incluye servidores como Tomcat, Jetty o Undertow, lo que permite ejecutar la aplicación sin necesidad de desplegarla en un servidor externo.
- **Inicio Rápido de Proyectos:** Con herramientas como Spring Initializr, se pueden generar proyectos base en cuestión de minutos.
- **Actuadores (Actuators):** Proporciona endpoints para monitoreo y gestión de la aplicación.
- **Mínima Configuración XML:** Se enfoca en convenciones y anotaciones, reduciendo la necesidad de archivos XML de configuración.

### 2.3. Primer proyecto

#### Spring initializr

![Spring Initializr](/assets/img/initializr.png)

- Genera un esqueleto de proyecto para importar.
- Accede a: <https://start.spring.io/>
- Elige gradle o maven. Depende de las dependencias y el archivo de configuración. Nosotros vamos a trabajar con maven.
  - gradle → `build.gradle`
  - maven → `pom.xml`
- Lenguaje de programación: Java.
- Versión de Spring Boot → Trabajar con la última disponible (viene por defecto, no SNAPSHOT ni M, ya que están en desarrollo).

Configuramos los metadatos:

- **Group:** dominio de la empresa o ejercicio (al revés `com.dominio`).
- **Artifact:** nombre con el que se va a identificar por ejemplo para importar (sin espacios).
- **Nombre del proyecto.**
- **Descripción.**
- **Nombre del paquete:** dónde se guardará la estructura de carpetas del proyecto.
- **Packaging:**
  - **jar** (java archive): contiene servidor y aplicación sin necesidad de servidor externo.
  - **war** (web application archive): para crear una aplicación web, se tienen que desplegar en un servidor web externo como Tomcat. Nosotros usaremos esta opción.
- **Java**, la versión LTS que tengamos instalada → 21 en nuestro caso.

**Dependencias:** componentes o bibliotecas que la aplicación necesita para funcionar.

- **Spring Web:** para usar apis RESTFUL y genera servidor tomcat → Añadir siempre.
- **Spring Data JPA:** Para interactuar con bases de datos utilizando JPA.
- **Spring Security:** Para añadir seguridad a la aplicación.
- **Thymeleaf:** Para generar vistas HTML desde el backend.
- **H2 Database:** Base de datos en memoria para pruebas rápidas.

**Generar** → se genera un archivo comprimido con el nombre que hemos puesto en Artifact listo para importar en nuestro IDE.

#### Práctica

Crea un proyecto de ejemplo siguiendo las indicaciones dadas en los apartados anteriores, puedes ayudarte del vídeo adjunto.

De momento no tienes que realizar ninguna entrega solo crear el proyecto con Spring Initializr.

#### Trabajar con el proyecto

- Descomprimir el proyecto generado.
- Abrir IntelliJ y seleccionar Open para abrirlo. Cuidado, no abrir ninguna carpeta dentro del proyecto sino la carpeta principal, la que tiene el nombre que pusimos en los metadatos en Artifact.
- Esperamos a que cargue las dependencias. Aceptar los mensajes que salgan en la esquina inferior derecha.
- **pom.xml** → Archivo de configuración con información, dependencias etc…
- **src:** alberga toda la lógica del proyecto. Si da error al importarlo, ha sido algún error en cargar las dependencias, volver a generar en Spring Initializr.
- Se levanta en el puerto **8080**, aunque se puede modificar en resources, `application.properties` (nosotros no lo vamos a hacer de momento).
- Al **ejecutar**, se levanta el servidor pero nuestra aplicación aún no tiene lógica por lo que al entrar en <http://localhost:8080> ¿qué pasará?.

#### Ejecutando la Aplicación

- Para ejecutar la aplicación, puedes:
  - Desde el IDE: Ejecutar la clase `DemoApplication.java`.
  - Desde la línea de comandos.
  - Desde un entorno de desarrollo simplemente ejecutando con el botón correspondiente.
- **Verificación.**
- Abre un navegador web y accede a <http://localhost:8080>. Deberías ver un error 404, lo cual es normal ya que no hemos definido ningún controlador aún.

## 3. Anatomía de una Aplicación Spring Boot

### Estructura

```text
demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── ejemplo/
│   │   │           └── demo/
│   │   │               └── DemoApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
│       └── java/
│           └── com/
│               └── ejemplo/
│                   └── demo/
│                       └── DemoApplicationTests.java
└── pom.xml
```

### 3.1. Ficheros importantes

- **pom.xml (o build.gradle):** Este archivo gestiona las dependencias y el ciclo de vida del proyecto. En el caso de Maven, `pom.xml` define las dependencias, como Spring Web, así como plugins para construir y ejecutar la aplicación.
- **application.properties (o application.yml):** Este archivo contiene la configuración de la aplicación, como el puerto del servidor, detalles de la base de datos, y ajustes de logs.
- **src/main/java:** Aquí reside el código fuente de la aplicación, incluyendo controladores, servicios y repositorios. La clase principal con la anotación `@SpringBootApplication` también se encuentra aquí.
- **src/main/resources:** Contiene recursos como plantillas Thymeleaf, archivos estáticos (CSS, JS), y `application.properties`.
- **src/test/java:** Esta carpeta alberga las pruebas unitarias y de integración, permitiendo verificar el correcto funcionamiento de la aplicación.

### 3.2. Clase principal

En una aplicación Spring Boot, la clase principal es aquella que arranca la aplicación, normalmente se coloca en la raíz del proyecto y lleva la anotación `@SpringBootApplication`.

Un ejemplo básico sería:

```java
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### 3.3. Archivo application.properties

Este archivo se utiliza para configurar propiedades de la aplicación.

```properties
# Configuracion para el acceso a la Base de Datos
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.globally_quoted_identifiers=true

# Puerto donde escucha el servidor una vez se inicie
server.port=8081

# Datos de conexion con la base de datos MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/myshoponline
spring.datasource.username=myshopuser
spring.datasource.password=mypassword
spring.datasource.driverClassName=com.mysql.jdbc.Driver
```

## 4. Web estática vs web dinámica

![Web estática vs web dinámica](/assets/img/est-din.jpg)

### 4.1. Página web estática

El contenido es siempre el mismo para todos los usuarios: se sirve tal cual está guardado en el servidor, sin procesamiento adicional (HTML, CSS, JS, imágenes).

### 4.2. Página web dinámica

El contenido se genera o modifica en función de datos, parámetros o interacciones del usuario (por ejemplo, consultando una base de datos o procesando el resultado de un formulario) antes de enviarse al cliente.

## 5. Creación de páginas web con Spring Boot

### 5.1. Anotaciones más comunes

Las **anotaciones** (`@Anotacion`) son metadatos que se añaden a clases, métodos o atributos para indicarle a Spring qué papel cumple ese elemento dentro de la aplicación, sin necesidad de escribir configuración adicional. Spring lee estas anotaciones en tiempo de ejecución y, en función de ellas, crea y gestiona automáticamente los objetos (beans), asocia rutas HTTP a métodos, inyecta dependencias o valida datos.

- **`@RestController`:** Define una clase como controlador REST.
- **`@RequestMapping`:** Asocia solicitudes HTTP a métodos del controlador.
- **`@GetMapping`:** Maneja solicitudes HTTP GET.
- **`@PostMapping`:** Maneja solicitudes HTTP POST.
- **`@PutMapping`:** Maneja solicitudes HTTP PUT.
- **`@DeleteMapping`:** Maneja solicitudes HTTP DELETE.
- **`@PatchMapping`:** Maneja solicitudes HTTP PATCH.
- **`@PathVariable`:** Extrae valores de variables en la URL.
- **`@RequestParam`:** Obtiene parámetros de la consulta o formulario.
- **`@RequestBody`:** Vincula el cuerpo de la solicitud a un parámetro.
- **`@ResponseBody`:** Devuelve datos directamente en la respuesta HTTP.
- **`@Autowired`:** Inyecta dependencias automáticamente.
- **`@Service`:** Declara una clase como un servicio de negocio.
- **`@Component`:** Declara una clase como un componente genérico de Spring.
- **`@Repository`:** Declara una clase como componente de acceso a datos.
- **`@Configuration`:** Declara una clase como fuente de configuración.
- **`@Bean`:** Declara un bean gestionado por Spring.
- **`@Value`:** Inyecta valores desde el archivo de propiedades.
- **`@Qualifier`:** Especifica cuál bean inyectar cuando hay múltiples candidatos.
- **`@Primary`:** Define un bean como principal cuando hay varios del mismo tipo.
- **`@RequestHeader`:** Extrae valores de encabezados HTTP.
- **`@CrossOrigin`:** Habilita CORS para peticiones desde otros dominios.
- **`@ExceptionHandler`:** Maneja excepciones específicas dentro de un controlador.
- **`@ControllerAdvice`:** Define manejo global de errores para controladores.
- **`@Valid`:** Activa validación sobre los objetos del modelo.
- **`@NotNull`:** Indica que un valor no puede ser nulo (usado con validación).
- **`@Min` / `@Max`:** Define valores mínimo o máximo permitidos en validaciones.
- **`@EnableAutoConfiguration`:** Habilita configuración automática en Spring Boot.
- **`@SpringBootApplication`:** Agrupa `@Configuration`, `@EnableAutoConfiguration` y `@ComponentScan`.

### 5.2. Estructura típica por capas

En un proyecto Spring Boot real, el código de `src/main/java` se organiza en paquetes según su responsabilidad, separando cada capa de la arquitectura:

- **`controller`:** clases anotadas con `@RestController` o `@Controller` que reciben las peticiones HTTP y devuelven la respuesta. No deben contener lógica de negocio.
- **`service`:** clases anotadas con `@Service` que contienen la lógica de negocio de la aplicación. Los controladores llaman a los servicios.
- **`repository`:** interfaces o clases anotadas con `@Repository` encargadas del acceso a datos (bases de datos, ficheros, APIs externas).
- **`model` (o `entity`):** clases que representan las entidades/tablas de la base de datos.
- **`dto`:** *Data Transfer Object*, clases que definen qué datos viajan entre el cliente y el servidor (petición/respuesta), evitando exponer directamente las entidades.
- **`config`:** clases anotadas con `@Configuration` para configuraciones específicas (seguridad, CORS, beans manuales, etc.).

```text
com.ejemplo.demo/
├── DemoApplication.java
├── controller/
│   └── UsuarioController.java
├── service/
│   └── UsuarioService.java
├── repository/
│   └── UsuarioRepository.java
├── model/
│   └── Usuario.java
├── dto/
│   ├── UsuarioRequestDTO.java
│   └── UsuarioResponseDTO.java
└── config/
    └── SecurityConfig.java
```

Con esta separación, una petición HTTP fluye así: **Controller** (recibe la petición y el DTO) → **Service** (aplica la lógica de negocio) → **Repository** (accede a los datos) → **Model** (entidad persistida), devolviendo la respuesta de nuevo como DTO hasta el cliente.

### 5.3. Carpeta resources

Carpeta `src/main/resources`. Contiene archivos incluidos en el classpath de la aplicación. Se usa para:

- **Configuración:** `application.properties` o `application.yml`.
- **Archivos estáticos:** en carpetas `static/`, `public/`, `resources/`, `META-INF/resources/`.
- **Plantillas HTML:** en `templates/` (usado con Thymeleaf, FreeMarker, etc.).
- **Otros recursos:** archivos `.json`, `.xml`, `.sql`, propiedades, etc.

Todo se empaqueta en el `.jar` y Spring lo carga automáticamente.


## 6. Creación de Páginas Web Estáticas en Spring Boot

Spring Boot permite servir páginas web estáticas fácilmente, sin necesidad de controladores, solo colocando archivos en el lugar correcto.

Simplemente habría que seguir estos pasos:

1. Crear un proyecto Spring Boot (puede ser con Spring Initializr).
2. Colocar archivos HTML en `/static` o `/public`. Ruta: `src/main/resources/static/` → Ejemplo: `src/main/resources/static/index.html`.
3. Ejecutar la aplicación.
4. Spring Boot detecta automáticamente los archivos estáticos.

**Acceso desde el navegador:**

- <http://localhost:8080/> → carga `index.html` automáticamente.
- <http://localhost:8080/about.html> → carga `about.html`.

> **IMPORTANTE:** No se necesita controlador para servir archivos en `static/` o `public/`.

#### Actividad: crear una página web estática

Crea un proyecto Spring Boot con Spring Initializr (dependencia Spring Web).

- Añade en `src/main/resources/static/index.html` una página de bienvenida con tu nombre y una breve presentación.
- Añade una segunda página `contacto.html` en la misma carpeta con algún dato de contacto ficticio.
- Ejecuta la aplicación y comprueba que:
  - <http://localhost:8080/> carga `index.html` sin necesidad de ningún controlador.
  - <http://localhost:8080/contacto.html> carga `contacto.html`.
- Añade también una hoja de estilos `static/css/estilos.css` y enlázala desde ambas páginas para comprobar que Spring Boot también sirve los recursos estáticos que no son HTML.

## 7. Creación de Páginas Web Dinámicas en Spring Boot

Spring Boot permite generar páginas web dinámicas (contenido personalizado según datos) usando motores de plantillas como Thymeleaf, FreeMarker, etc. o cargando nuestros propios archivos `.html`.

1. **Crear proyecto Spring Boot:** con dependencia Spring Web + Thymeleaf (o FreeMarker).
2. **Crear controlador (Controller):** devuelve vistas con datos dinámicos.
3. **Crear plantillas HTML:** en carpeta `src/main/resources/templates/`.

> **IMPORTANTE:** Para servir una página web dinámica se necesita obligatoriamente un controlador.

### 7.1. Introducción a controladores

Un controlador en Spring Boot es una clase que maneja las solicitudes HTTP entrantes, procesa la lógica necesaria y devuelve respuestas, ya sean vistas HTML o datos (JSON, XML).

**Funciones:**

- Recibir peticiones del cliente (GET, POST, etc.).
- Ejecutar lógica o llamar a servicios.
- Devolver una respuesta adecuada (página web, JSON, redirección, etc.).

## 8. Métodos HTTP en Spring Boot

### 8.1. Método GET

El método GET se usa para solicitar datos al servidor sin modificar nada. En Spring Boot, se mapea a métodos del controlador para manejar esas solicitudes.

**Cómo se usa:**

- Se anota el método del controlador con `@GetMapping` (o `@RequestMapping` con método GET).
- Se define la ruta que responderá a la solicitud GET.
- El método retorna datos o una vista.
- Se puede acceder a los datos enviados usando `@RequestBody`, `@RequestParam`.

Al acceder a <http://localhost:8080/hola> el servidor responde con: "¡Hola desde GET!"

```java
@RestController
public class HolaControlador {

    @GetMapping("/hola")
    public String decirHola() {
        return "¡Hola desde GET!";
    }
}
```

### 8.2. Método POST

El método POST se usa para enviar datos al servidor, generalmente para crear o modificar recursos. A diferencia de GET, POST envía información en el cuerpo de la solicitud (no en la URL) y puede cambiar el estado del servidor.

**Cómo se usa:**

- El cliente envía datos (por ejemplo, un formulario, JSON) al servidor mediante POST.
- El servidor recibe esos datos, los procesa (ejemplo: guarda en base de datos).
- El servidor devuelve una respuesta indicando éxito, error, etc.
- Se usa la anotación `@PostMapping` para manejar solicitudes POST.

El cliente envía un POST a `/usuario` con el nombre en el cuerpo y el servidor responde confirmando la creación con el nombre recibido:

```java
@RestController
public class UsuarioControlador {

    @PostMapping("/usuario")
    public String crearUsuario(@RequestBody String nombre) {
        return "Usuario creado: " + nombre;
    }
}
```

**Ejemplo recibiendo un objeto completo en JSON**

Queremos recibir los datos de un usuario en JSON mediante POST y guardarlos en un objeto. Lo primero sería definir nuestra clase `Usuario`:

```java
public class Usuario {
    private String nombre;
    private int edad;

    // Constructor vacío (necesario para deserialización)
    public Usuario() {}

    // Getters y setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
}
```

A continuación definimos el controlador y la ruta que recogerá los datos:

```java
@RestController
public class UsuarioControlador {

    @PostMapping("/usuario")
    public String crearUsuario(@RequestBody Usuario usuario) {
        return "Usuario creado: " + usuario.getNombre() + ", edad: " + usuario.getEdad();
    }
}
```

**¿Cómo funciona?**

- El cliente envía un POST a `/usuario` con el JSON en el cuerpo.
- Spring automáticamente convierte ese JSON en el objeto `Usuario`.
- El método accede a los datos usando los getters.
- Responde con un mensaje que incluye los datos recibidos.

El formato del JSON enviado para que esto funcione será:

```json
{
    "nombre": "Ana",
    "edad": 30
}
```

**¿Cómo enviar JSON en el cuerpo de una petición POST usando Postman?**

- Selecciona método POST.
- Ingresa la URL, por ejemplo: `http://localhost:8080/usuario`.
- Ve a la pestaña Body.
- Elige la opción `raw` y luego en el desplegable selecciona `JSON`.
- Escribe el JSON.

## 9. JSON

JSON (JavaScript Object Notation) es un formato de texto ligero para el intercambio de datos.

Está compuesto por pares clave-valor. Utiliza dos estructuras principales:

- **Objeto:** conjunto de pares clave-valor, delimitado por `{}`.
- **Arreglo (Array):** lista ordenada de valores, delimitada por `[]`.

```json
{
  "clave1": "valor1",
  "clave2": "valor2",
  "clave3": {
    "subclave": "subvalor"
  }
}
```

```json
[
  "valor1",
  "valor2",
  "valor3"
]
```

**Tipos de valores**

- **Cadenas de texto (string):** `"Hola Mundo"`
- **Números:** `123`, `45.67`
- **Booleanos:** `true`, `false`
- **Null:** `null`
- **Objetos:** `{ "clave": "valor" }`
- **Arrays:** `[1, 2, 3]`

```json
{
  "nombre": "Ana",
  "edad": 30,
  "esEstudiante": false,
  "hobbies": ["leer", "futbol", "cine"],
  "direccion": {
    "calle": "Av. Siempre Viva",
    "numero": 742
  }
}
```

## Actividades

### Actividad 1

Sobre el proyecto de prueba que creamos en la práctica anterior realiza las siguientes acciones:

**Personaliza el mensaje de inicio.**

Modifica el archivo `application.properties` para añadir un banner personalizado al iniciar la aplicación.

> Pista: Usa la propiedad `spring.banner.location`.

**Cambio de Puerto.**

Cambia el puerto de la aplicación a 9090 y verifica que la aplicación responde en el nuevo puerto.

`spring.banner.location` es una propiedad de configuración de Spring Boot que te permite definir la ubicación de un banner personalizado que aparece al iniciar tu aplicación. Por defecto, Spring Boot busca un archivo llamado `banner.txt` en `src/main/resources`.

Si creamos un archivo `banner.txt` con el contenido que deseemos automáticamente aparecerá al iniciar Spring Boot.

La propiedad `spring.banner.location` la utilizaremos cuando queremos cambiar la ruta por defecto. Por ejemplo:

```properties
spring.banner.location=file:/opt/banners/mi-banner.txt
```

- **Creación de un controlador simple.**
  - Crea una clase `ControladorSaludo` en el paquete `com.ejemplo.demo`.
  - Define un método que responda a la ruta `/saludo` y que retorne el texto "¡Hola, Spring Boot!".
  - Utiliza para el controlador el código de ejemplo proporcionado.
  - ¿Qué significarán las anotaciones (`@`) de la aplicación?

```java
package com.ejemplo.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ControladorSaludo {

    @GetMapping("/saludo")
    public String saludo() {
        return "¡Hola, Spring Boot!";
    }
}
```

### Actividad 2

Crea un proyecto Spring Boot con el nombre `actividad2` utilizando Spring Initializr.

El proyecto debe incluir un único controlador que exponga dos endpoints diferentes:

- **inicio** debe devolver un mensaje en HTML que muestre un título con el texto "Bienvenido a la aplicación" y un párrafo breve de presentación.
- **contacto** debe devolver un mensaje en HTML que muestre un título con el texto "Página de contacto" y un párrafo con información ficticia de contacto (por ejemplo, un correo electrónico o un número de teléfono).

#### Actividad guiada: recogida de parámetros

Pongamos que queremos hacer un controlador que salude al usuario por su nombre, para ello el usuario nos debe indicar cómo se llama.

Mediante GET el usuario en la URL puede indicar parámetros con el siguiente formato:

<http://localhost:8080/hola?name=Ana>

En el controlador se debe de recoger el valor Ana para así saludar al usuario y devolver: "Hola Ana".

Para recoger parámetros se utiliza la anotación `@RequestParam` en la que debemos indicar el nombre del parámetro. Tiene el siguiente formato:

```java
@RestController
public class demoController {

    @GetMapping("/hola")
    public String hello(@RequestParam(name = "name", required = false) String name) {
        if (name == null || name.isEmpty()) {
            name = "Desconocido";
        }
        return "Hola " + name;
    }
}
```

#### Diferencia entre @RestController y @Controller

**@Controller**

- Es la anotación clásica de Spring MVC para controladores que manejan vistas web (HTML, JSP, Thymeleaf, etc.)
- Los métodos típicamente retornan el nombre de una vista y Spring se encarga de resolverla.
- No devuelven automáticamente datos (JSON, XML, etc.)

**@RestController**

- Devuelve datos directamente en el cuerpo de la página web.
- Ideal para APIs REST: todos los métodos retornan datos (JSON, XML, texto), no vistas.

### Actividad 3

Crea un proyecto Spring Boot con el nombre `actividad3` utilizando Spring Initializr.

- Crea una página estática de inicio llamada `index.html` que de la bienvenida y explique el funcionamiento con los endpoints disponibles.
- Crea 4 páginas estáticas: `spanish.html`, `french.html`, `english.html` y `german.html`. En cada una de ellas debe de haber un texto en el idioma indicado en el título.
- El usuario deberá introducir en la URL el idioma elegido mediante el parámetro `idioma`.
- La aplicación deberá recoger el parámetro del usuario y abrir la página correspondiente al idioma elegido.
- Si no existe ningún parámetro por defecto se abrirá el idioma inglés.

> PISTA:
>
> ```java
> return "redirect:/pagina";
> ```

### Actividad 4

Crea una aplicación en Spring Boot que exponga un endpoint GET para generar dinámicamente una tabla HTML en función de los parámetros introducidos por el usuario con nombre `actividad4`.

URL base: `/tabla`

Parámetros:

- **filas:** número de filas de la tabla.
- **columnas:** número de columnas de la tabla.

Ejemplo de petición:

<http://localhost:8080/tabla?filas=5&columnas=3>

#### Validaciones

- El número de filas y columnas debe estar dentro del rango 1–20.
- Si los valores están fuera de rango, deben corregirse y tomarse los valores máximo/mínimo.
- Controlar también el caso de valores no numéricos o parámetros ausentes.

#### Salida esperada

- La respuesta debe ser código HTML válido con una tabla.
- Debe incluir un encabezado con las columnas numeradas.
- Cada celda del cuerpo debe mostrar el texto: Fila X, Columna Y, donde X e Y son los números correspondientes.

### Actividad 5

**Ejemplo con POST**

Crea un servicio REST con Spring Boot que permita recibir por un endpoint POST un pedido de compra que contenga los datos del cliente y una lista de productos.

El pedido debe tener:

- Información del cliente: nombre, correo electrónico y teléfono.
- Una lista de productos, donde cada producto tiene: nombre, cantidad y precio unitario.

El endpoint debe recibir este pedido en formato JSON, calcular el total del pedido (sumando el precio unitario * cantidad de cada producto), y devolver una respuesta con un resumen que incluya el nombre del cliente, la cantidad total de productos y el precio total a pagar.

No es necesario guardar la información en base de datos; solo procesar y devolver la respuesta.

Ejemplo de formato de entrada (pedido):

```json
{
  "cliente": {
    "nombre": "María Pérez",
    "correo": "maria.perez@email.com",
    "telefono": "123456789"
  },
  "productos": [
    {
      "nombre": "Teclado",
      "cantidad": 2,
      "precioUnitario": 25.50
    },
    {
      "nombre": "Mouse",
      "cantidad": 1,
      "precioUnitario": 15.00
    },
    {
      "nombre": "Monitor",
      "cantidad": 1,
      "precioUnitario": 150.00
    }
  ]
}
```

Ejemplo de salida:

```json
{
  "cliente": "María Pérez",
  "totalProductos": 4,
  "precioTotal": 216.00,
  "mensaje": "Pedido recibido correctamente"
}
```

#### POST - Formulario

Creamos nuestro formulario y lo ubicamos en la carpeta `/templates`:

```html
<!DOCTYPE html>
<html>
<head>
    <title>Formulario</title>
</head>
<body>
    <form action="/procesar" method="post">
        <label>Nombre:</label>
        <input type="text" name="nombre" /><br/>

        <label>Edad:</label>
        <input type="number" name="edad" /><br/>

        <button type="submit">Enviar</button>
    </form>
</body>
</html>
```

Creamos el controlador:

```java
package com.example.formulario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FormController {

    @GetMapping("/formulario")
    public String mostrarFormulario() {
        return "formulario";
    }

    @PostMapping("/procesar")
    @ResponseBody
    public String procesarFormulario(
            @RequestParam("nombre") String nombre,
            @RequestParam("edad") int edad) {

        return "Nombre: " + nombre + ", edad: " + edad;
    }
}
```

Probamos el funcionamiento. Al darle a enviar rellenando datos:

```text
Nombre: Matías, edad: 17
```

**Detalles importantes:**

- Ya estamos trabajando con plantillas por lo que hay que añadir la dependencia Thymeleaf.
- `@ResponseBody` significa que lo que retornes desde el método se envía directamente como texto en la respuesta HTTP, si no lo ponemos ¿qué pasa?
- `@RequestParam("nombre")` → recoge el campo `<input name="nombre"/>`.
- El nombre dentro de `@RequestParam("...")` debe coincidir con el atributo `name` del formulario.
- Si el parámetro es opcional y/o tiene un valor por defecto puedes marcarlo así:

```java
@RequestParam(value = "edad", required = false, defaultValue = "0") int edad
```

### Actividad 6

**POST con formulario**

Desarrollar una aplicación web en Spring Boot que permita a los usuarios registrarse a un sistema mediante un formulario.

Mostrar un formulario HTML para ingresar los siguientes datos del usuario:

- **Nombre:** obligatorio.
- **Apellidos:** opcional, si no se introduce valor por defecto cadena vacía.
- **Correo electrónico:** obligatorio.
- **Contraseña:** opcional, si no se introduce crear una contraseña aleatoria de 6 dígitos numéricos (utilizar `Random`).

Al enviar el formulario:

- Capturar los datos en un objeto Java (modelo).
- Mostrar una página de confirmación que muestre el nombre completo del usuario, su correo electrónico y su contraseña en caso de que se haya generado automáticamente, si el usuario introdujo contraseña no se mostrará.
- La confirmación se enviará directamente al cuerpo de la respuesta, sin plantilla.

### Actividad 7

En la actividad 6 hemos creado un formulario de registro y hemos recogido los datos en un modelo, pues bien, continuando en este sentido vamos a completarla haciendo un formulario de login, para ello:

- Crearemos un formulario con los campos usuario y contraseña.
- Comprobaremos que el usuario existe y la contraseña está correcta.
- Devolveremos el mensaje correspondiente (usuario logado, usuario no existe, contraseña incorrecta etc.) al cuerpo de la respuesta, sin plantilla.

Para ello, como no estamos trabajando con bases de datos, deberíamos tener creada alguna colección en memoria donde almacenemos unos cuantos usuarios de prueba.
