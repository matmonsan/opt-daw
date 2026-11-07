---
title: Versión 4
description: "<strong>Módulo:</strong> Programación Web Avanzada <br> <strong>Profesor:</strong> Matías Montávez Sánchez"
---
[⌂ Volver al enunciado del proyecto](../enunciado_proyecto.md)

# **FECHA DE ENTREGA:** 26/02/2027

# Versión 4 — Compra, administración, informes y despliegue

## Objetivo

Completar la tienda: el cliente podrá tramitar sus pedidos y gestionar su área personal; el administrador tendrá un panel para el catálogo, los pedidos, los usuarios y las estadísticas; se añadirán las descargas de los informes Excel de la Versión 3 al área de cliente y al panel; y la aplicación completa quedará desplegada. Con esta versión se cierra el proyecto.

## Planificación orientativa

| Semana | Trabajo |
|---|---|
| 13/02 - 19/02 | Apartado 1 y comienzo del 2: compra, área de cliente y panel de administración |
| 20/02 - 26/02 | Final del apartado 2 y apartados 3 y 4: panel, despliegue y cierre |

---

## Enunciado

### 1. Compra y área de cliente

Tramitación (`/compra`):

- Resumen del pedido y dirección de envío precargada desde el perfil y editable.
- Botón *Confirmar pedido* que llama a `POST /api/pedidos`, desactivado mientras se procesa para evitar pedidos duplicados.
- Si la API responde `409` por stock, se indica qué productos fallan y se vuelve al carrito.
- Página de pedido realizado con el código y un enlace al detalle. El contador del carrito pasa a 0.

Mis pedidos (`/pedidos` y `/pedidos/:id`):

- Listado paginado con código, fecha, estado (distintivo de color), número de artículos y total; filtro por estado.
- Detalle con líneas, importes, dirección y línea de tiempo de estados.
- Cancelar pedido, solo si el estado lo permite y con confirmación.
- Repetir pedido: añade al carrito las líneas del pedido y lleva al carrito.
- Descargar Excel del pedido (`GET /api/pedidos/{id}/exportar`), reutilizando la descarga con token de la Versión 3.

Mi perfil (`/perfil`): modificar nombre y dirección de envío. Los errores de validación de la API se muestran junto a cada campo, con un mecanismo reutilizable (por ejemplo, un *composable* `useErroresApi`) que se usará también en la administración.

### 2. Panel de administración

Rutas anidadas bajo `/admin`, solo para administradores, con un menú lateral propio:

- Resumen con los datos de `GET /api/admin/estadisticas`: tarjetas con ventas, pedidos y ticket medio; al menos dos gráficas (pedidos por estado y ventas por categoría, por ejemplo con Chart.js y `vue-chartjs`); productos más vendidos y con stock bajo; selector de rango de fechas y botón para descargar el Excel de pedidos del rango (`GET /api/admin/pedidos/exportar`).
- Categorías: tabla, alta, edición y borrado con confirmación (explicando el `409` si tiene productos).
- Productos: tabla paginada con búsqueda y filtros; un mismo formulario para crear y editar con validación; vista previa de la imagen; ajuste rápido de stock; retirar, reactivar y marcar como destacado.
- Pedidos: tabla paginada con filtros por estado, fechas y cliente; detalle; cambio de estado ofreciendo solo las transiciones válidas.
- Usuarios: tabla paginada y cambio de rol con confirmación.

### 3. Investigación: despliegue
{: .investigacion}

> Este apartado no se explica en clase. Es trabajo de investigación y debe documentarse en la memoria.
{: .investigacion}

Elegid una opción:

- **Docker Compose:** `Dockerfile` *multi-stage* para el backend (compilar con Maven y ejecutar con una imagen de JRE) y para el frontend (`npm run build` servido con Nginx, configurado para que las rutas de Vue funcionen al recargar). `docker compose up --build` deja la tienda completa funcionando en `http://localhost`.
- **Nube:** base de datos gestionada, API en Render, Railway o similar, y Vue en Netlify, Vercel o similar, con las variables de entorno, CORS y los dominios autorizados en el proveedor de autenticación configurados.

En ambos casos, usad un perfil de producción de Spring (`application-prod.properties`) y no incluyáis secretos en las imágenes ni en el repositorio.

### 4. Cierre del proyecto

`README.md` final con descripción, capturas, tecnologías, cómo arrancar en desarrollo y con Docker (o URL del despliegue), usuarios de prueba y enlaces a Swagger, a las hojas de prueba y a la colección de Postman. Fusión en `main` y etiqueta `v4`.

---

## Requisitos

- Ciclo completo de compra, historial, cancelación, repetir pedido y perfil desde la interfaz.
- Panel de administración accesible solo para administradores, con interfaz para todas las rutas `/api/admin/`, formulario reutilizable y solo las transiciones de estado válidas.
- Estadísticas con al menos dos gráficas.
- Descarga de los informes Excel desde *Mis pedidos* y desde el resumen de administración.
- Aplicación completa desplegada o arrancable con un comando, con perfil de producción y sin secretos.
- Todas las acciones destructivas piden confirmación y todos los errores se muestran de forma comprensible.

## Reparto en la pareja

| Persona A | Persona B |
|---|---|
| Tramitación del pedido y perfil | *Mis pedidos*: listado, detalle, cancelar y repetir |
| Resumen de administración con gráficas, categorías y productos | Pedidos y usuarios de administración |
| Despliegue del backend y la base de datos | Despliegue del frontend y `README.md` final |

## Criterios de finalización

- Un cliente puede comprar, consultar, descargar, cancelar y repetir un pedido.
- Un administrador puede crear un producto y verlo al momento en el catálogo, y llevar un pedido de `PENDIENTE` a `ENTREGADO` mientras el cliente lo ve actualizado.
- Un pedido descargado en Excel puede volver a importarse en el carrito.
- La tienda completa funciona en el entorno desplegado.

## Entrega

Incluid en la memoria:

- Capturas del flujo de compra, del área de cliente y de cada sección del panel de administración.
- Investigación sobre el despliegue: opción elegida, diagrama de la arquitectura desplegada, configuración y gestión de secretos.
- Valoración final del proyecto por cada persona de la pareja.

Consultad los requisitos comunes de entrega en el [enunciado general](../enunciado_proyecto.md#contenido-de-cada-entrega).

---

[← Versión 3](version_3.md) · [⌂ Volver al enunciado del proyecto](../enunciado_proyecto.md)
