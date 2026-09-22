[⌂ Volver al inicio](index.md)

## Índice

1. [Introducción a la programación web](#1-introducción-a-la-programación-web)
2. [Arquitecturas de aplicaciones web](#2-arquitecturas-de-aplicaciones-web)
   - [Arquitectura cliente-servidor](#21-arquitectura-cliente-servidor)
   - [Arquitectura de tres capas](#22-arquitectura-de-tres-capas)
   - [Arquitectura de microservicios](#23-arquitectura-de-microservicios)
3. [Tecnologías del lado del servidor](#3-tecnologías-del-lado-del-servidor)
   - [Lenguajes de programación](#31-lenguajes-de-programación)
   - [Frameworks](#32-frameworks)
   - [Bases de datos](#33-bases-de-datos)
4. [Protocolos y comunicación en la web](#4-protocolos-y-comunicación-en-la-web)
   - [HTTP y HTTPS](#41-http-y-https)
   - [APIs RESTful](#42-apis-restful)
5. [Tendencias actuales en la programación web](#5-tendencias-actuales-en-la-programación-web)
6. [Actividades](#actividades)

## 1. Introducción a la programación web

La programación web consiste en el desarrollo de aplicaciones y servicios que se ejecutan en un entorno web utilizando navegadores como interfaz de usuario.

Combina tecnologías del lado del cliente y del servidor para crear experiencias interactivas y dinámicas.

## 2. Arquitecturas de aplicaciones web

La arquitectura de una aplicación web define su estructura y cómo sus componentes interactúan entre sí. A continuación, se describen las arquitecturas más comunes.

### 2.1. Arquitectura cliente-servidor

En esta arquitectura, el cliente, generalmente un navegador web, solicita recursos o servicios al servidor, que procesa la solicitud y devuelve una respuesta.

#### Características

- Existen dos roles:
  - **Cliente:** solicita servicios.
  - **Servidor:** proporciona información o funcionalidad.
- El cliente inicia el proceso con una solicitud y el servidor responde.
- La comunicación se realiza mediante un protocolo de solicitudes y respuestas.
- Cliente y servidor suelen estar en máquinas distintas conectadas por red, aunque pueden estar en la misma máquina.
- En la web, los navegadores actúan como clientes.
- El servidor devuelve la página solicitada o un mensaje de error.
- La comunicación utiliza HTTP o HTTPS.
- Los puertos reservados son el **80** para HTTP y el **443** para HTTPS.
- El modelo cliente-servidor y el protocolo HTTP condicionan el desarrollo de aplicaciones web.

### 2.2. Arquitectura de tres capas

La arquitectura a tres capas es una ampliación del modelo cliente-servidor.

#### Características

Se divide en:

- **Capa de presentación:** contiene la interfaz de usuario y muestra la información y la interacción.
- **Capa de negocio:** contiene la lógica de la aplicación y conecta las demás capas.
- **Capa de datos:** gestiona la base de datos.

La capa de presentación no se comunica directamente con la capa de datos; siempre pasa por la capa de negocio.

#### Flujo de comunicación

```text
Usuario -> presentación -> negocio -> datos
Datos -> negocio -> presentación
```

En aplicaciones web:

- **Presentación:** navegador en el ordenador del usuario.
- **Negocio:** servidor web.
- **Datos:** servidor de bases de datos, que puede estar junto al servidor web o separado.

#### Objetivo

- Separar la lógica de negocio de la presentación para obtener código más reutilizable.
- Facilitar el mantenimiento y la ampliación de las aplicaciones.

### 2.3. Arquitectura de microservicios

Consiste en desarrollar la aplicación como un conjunto de servicios pequeños e independientes que se comunican entre sí.

#### Beneficios

- Despliegue y desarrollo independientes.
- Escalabilidad selectiva.
- Mayor tolerancia a fallos.

## 3. Tecnologías del lado del servidor

Las tecnologías del lado del servidor, también conocidas como tecnologías **backend**, son el conjunto de herramientas y lenguajes de programación que se ejecutan en un servidor web para procesar la información, interactuar con bases de datos y generar contenido dinámico antes de enviarlo al navegador del usuario.

### ¿Cómo funcionan?

1. **Petición del cliente.** Un usuario solicita una página o recurso desde su navegador, que actúa como cliente.
2. **Procesamiento en el servidor.** El servidor web recibe la petición, interpreta el script del lado del servidor, utilizando lenguajes como Python o PHP, y realiza tareas como consultar una base de datos.
3. **Generación de contenido dinámico.** El servidor genera el código HTML, CSS y JavaScript que el navegador entenderá.
4. **Respuesta al cliente.** El contenido generado se envía al navegador del usuario para su visualización.

### 3.1. Lenguajes de programación

- **Java:** orientado a objetos, robusto y multiplataforma. Se utiliza en aplicaciones empresariales y sistemas de gran escala.
- **Python:** sencillo y legible, con una sintaxis clara. Es popular en ciencia de datos y desarrollo web mediante frameworks como Django y Flask.
- **PHP:** cuenta con un amplio soporte en la web. Se utiliza en sistemas de gestión de contenidos como WordPress y en frameworks como Laravel.
- **JavaScript (Node.js):** permite ejecutar JavaScript en el servidor, unificando el lenguaje en frontend y backend. Es adecuado para aplicaciones en tiempo real.

### 3.2. Frameworks

Un **framework**, o marco de trabajo, es un conjunto de herramientas, bibliotecas, convenciones y una estructura predefinida que simplifica el desarrollo de aplicaciones. Proporciona una base para resolver problemas comunes y permite que los desarrolladores se enfoquen en la lógica específica del proyecto.

Al utilizar un framework se acelera el proceso de desarrollo, se estandarizan las prácticas, se promueve la reutilización de código y se mejora la calidad y escalabilidad del software. Los frameworks proporcionan una estructura estándar para desarrollar aplicaciones web, facilitan tareas comunes y permiten centrarse en la lógica de negocio.

#### Spring Boot (Java)

Spring Boot simplifica la creación de aplicaciones Java basadas en Spring, facilitando la configuración y el despliegue.

- **Configuración automática:** detecta las dependencias y configura automáticamente los componentes necesarios.
- **Servidor embebido:** incluye servidores como Tomcat o Jetty, evitando configuraciones adicionales.
- **Ecosistema amplio:** se integra con proyectos Spring como Spring Data y Spring Security.
- **Ideal para microservicios:** permite crear fácilmente microservicios independientes.

#### Django (Python)

Django es un framework de alto nivel que promueve el desarrollo rápido y el diseño limpio, siguiendo el principio de «No te repitas» (DRY).

- **ORM potente:** simplifica la interacción con bases de datos mediante objetos Python.
- **Panel de administración integrado:** genera automáticamente una interfaz administrativa para gestionar datos.
- **Seguridad incorporada:** protege contra ataques comunes como la inyección SQL y el cross-site scripting.
- **Escalabilidad y versatilidad:** es adecuado para proyectos pequeños y grandes aplicaciones.

#### Express.js (Node.js)

Express.js es un framework minimalista para Node.js que facilita la creación de aplicaciones web y APIs robustas.

- **Simplicidad y flexibilidad:** permite estructurar las aplicaciones de manera personalizada.
- **Middleware modular:** extiende la funcionalidad mediante componentes reutilizables.
- **Gran comunidad:** dispone de un amplio conjunto de paquetes y recursos.
- **Adecuado para aplicaciones en tiempo real:** resulta útil para aplicaciones que requieren comunicación bidireccional.

#### Laravel (PHP)

Laravel es un framework web moderno para PHP que sigue el patrón MVC y proporciona una sintaxis elegante y expresiva.

- **Enrutamiento avanzado:** ofrece una gestión de rutas sencilla y potente.
- **Eloquent ORM:** permite interactuar con bases de datos de forma intuitiva mediante modelos.
- **Motor de plantillas Blade:** crea vistas con una sintaxis limpia y heredable.
- **Herramientas integradas:** incluye sistemas de colas, eventos y tareas programadas.

### 3.3. Bases de datos

#### Bases de datos relacionales (SQL)

- **MySQL:** sistema de gestión de bases de datos relacional de código abierto, ampliamente utilizado en aplicaciones web.
- **PostgreSQL:** base de datos relacional avanzada con extensiones como el soporte para JSON y XML.
- **Oracle:** base de datos comercial robusta con características avanzadas para grandes volúmenes de datos.

#### Características de las bases de datos relacionales

- **Modelo relacional:** datos organizados en tablas con relaciones definidas.
- **Integridad de datos:** uso de claves primarias y foráneas.
- **Transacciones ACID:** garantizan la consistencia de las operaciones.

#### Bases de datos NoSQL

- **MongoDB:** base de datos orientada a documentos que almacena datos en formato BSON, similar a JSON.
- **Cassandra:** diseñada para manejar grandes cantidades de datos en múltiples servidores y altamente escalable.
- **Redis:** almacén de datos en memoria utilizado para almacenamiento en caché y sistemas en tiempo real.

#### Características de las bases de datos NoSQL

- **Flexibilidad en el modelo de datos:** permite almacenar datos no estructurados o semiestructurados.
- **Alto rendimiento y escalabilidad:** es adecuado para aplicaciones que requieren respuestas rápidas.
- **Variedad de tipos de almacenamiento:** documentos, pares clave-valor, grafos, entre otros.

## 4. Protocolos y comunicación en la web

En la web, un protocolo es un conjunto estandarizado de reglas que define cómo los dispositivos se comunican y transfieren datos entre sí, asegurando que los mensajes se envíen, reciban e interpreten correctamente.

### Funciones de los protocolos

- Establecer reglas de comunicación.
- Definir cómo los navegadores web y los servidores web intercambian información.
- Formatear y procesar datos.
- Determinar el formato de los datos para que sean entendibles por otros dispositivos.
- Asegurar la entrega correcta de los datos.
- Garantizar que los mensajes se entreguen e interpreten de manera adecuada en la red.
- Habilitar la comunicación entre dispositivos.
- Permitir que diferentes ordenadores y dispositivos se comuniquen entre sí de forma eficiente.

### 4.1. HTTP y HTTPS

#### HTTP (HyperText Transfer Protocol)

Es el protocolo base de la web y define cómo se formatean y transmiten los mensajes entre el cliente y el servidor.

- **Puerto predeterminado:** 80.
- **Comunicación en texto plano:** no cifrada.

#### HTTPS (HTTP Secure)

Es la versión segura de HTTP, que utiliza SSL/TLS para cifrar la comunicación.

- **Puerto predeterminado:** 443.
- **Comunicación cifrada:** protege la integridad y confidencialidad de los datos.

#### Métodos HTTP comunes

- **GET:** solicita la representación de un recurso.
- **POST:** envía datos al servidor.
- **PUT:** actualiza un recurso existente.
- **DELETE:** elimina un recurso.

### 4.2. APIs RESTful

Una API RESTful es una Interfaz de Programación de Aplicaciones que se adhiere a los principios de la Transferencia de Estado Representacional (REST) para la comunicación entre sistemas a través de Internet usando el protocolo HTTP.

Se basa en la idea de que los datos están organizados en recursos y se interactúa con ellos usando métodos HTTP como GET, POST, PUT y DELETE para obtener, enviar, actualizar o eliminar información en formatos como JSON o XML.

#### ¿Cómo funcionan?

- **Recursos:** los datos se organizan en recursos, que se identifican mediante un URI (Uniform Resource Identifier), por ejemplo `api.example.com/usuarios`.
- **Métodos HTTP:** definen la acción que se va a realizar:
  - **GET:** obtener información.
  - **POST:** enviar datos y crear un nuevo recurso.
  - **PUT:** actualizar un recurso existente.
  - **DELETE:** eliminar un recurso.
- **Representación de datos:** el cliente envía una solicitud HTTP y la API devuelve una representación del recurso solicitado, que puede estar en formato JSON, HTML o XML.

#### Ejemplo práctico

Cuando un usuario accede a un sitio web, el navegador, que actúa como cliente, envía una solicitud GET a la API para obtener el contenido de la página.

La API RESTful se conecta a la base de datos, recupera la información y la envía de vuelta en un formato que el navegador puede mostrar, creando una aplicación web interactiva.

## 5. Tendencias actuales en la programación web

- **Aplicaciones de una sola página (SPA):** utilizan frameworks como React, Angular o Vue.js para ofrecer experiencias más fluidas.
- **Computación serverless:** permite ejecutar código sin necesidad de gestionar servidores físicos.
- **DevOps y CI/CD:** integra y entrega software de forma continua para mejorar la eficiencia del desarrollo.
- **Inteligencia artificial y aprendizaje automático:** se integran en aplicaciones web para ofrecer experiencias personalizadas.

## Actividades

### Actividad 1. Arquitecturas web

Selecciona tres sitios web que utilices frecuentemente.

1. Identifica qué arquitectura crees que utiliza cada uno: cliente-servidor, tres capas o microservicios.
2. Justifica tu respuesta basándote en las características estudiadas.

### Actividad 2. Comparativa de tecnologías

Elabora una tabla comparativa de tres lenguajes de programación del lado del servidor.

Incluye los siguientes aspectos:

- Sintaxis.
- Frameworks disponibles.
- Casos de uso.
- Curva de aprendizaje.

### Actividad 3. APIs públicas

1. Escoge una API pública de las que se encuentran en el repositorio [public-apis de GitHub](https://github.com/public-apis/public-apis).
2. Explora el funcionamiento de los diferentes endpoints de la API REST.
3. Identifica los recursos disponibles y los métodos HTTP soportados.

> **Nota:** un endpoint es una URL específica de una API que permite acceder a un recurso o ejecutar una operación determinada.

### Actividad 4. Postman

Investiga qué es Postman y cuál es su función principal en el desarrollo de software.

- Describe las características más importantes de esta herramienta.
- Explica por qué es útil para programadores y testers.
- Elabora un informe breve donde presentes tus hallazgos.
- Incluye ejemplos de situaciones en las que se puede usar Postman.
- Incluye las webs y vídeos donde hayas encontrado la información.
