# DSS-P3-MUII
 # Catálogo de Productos App - Práctica 2

Este repositorio contiene el desarrollo de una aplicación para acceder a un catálogo de productos, implementada como parte de la **Práctica 3**. Esta aplicación móvil permite a los usuarios navegar y consultar productos en stock, así como gestionar la información de ubicación del almacén respecto a la posición actual del usuario.

## Descripción General

La aplicación está diseñada para integrarse con una **aplicación Web RESTful** creada en la Práctica 1, la cual provee las funcionalidades necesarias para la gestión del catálogo de productos y la implementación de los casos de uso típicos. Esta práctica se realizará en grupos de dos estudiantes y será presentada al resto de la clase en las fechas asignadas.

### Características

1. **Acceso al catálogo de productos**: La aplicación permite al usuario consultar el catálogo completo, disponible en la Web mediante un servidor RESTful.
   
2. **Gestión geográfica**: La app incluye funcionalidades para localizar el almacén en relación a la ubicación actual del usuario, haciendo posible la gestión de información geográfica.

3. **Interacción desde dispositivos móviles**: Al ejecutarse desde un dispositivo móvil, el usuario podrá acceder al catálogo a través de un navegador web, donde se mostrará una descripción detallada de los productos en stock.

4. **Acceso autorizado**: Solo el personal autorizado podrá añadir o eliminar productos en el catálogo desde la aplicación, asegurando un control adecuado del inventario.

5. **Carrito de compras**: Los usuarios pueden agregar productos a un carrito de compras personal y proceder con la compra desde la app.

### Requerimientos de Integración

La aplicación está dividida en dos grandes módulos:

- **Módulo A (70%)**: Integración con la aplicación Web desarrollada en la Práctica 1, proporcionando la funcionalidad básica de la app y los casos de uso esenciales. Este módulo permite añadir o eliminar productos desde la app, aunque solo el personal autorizado tendrá acceso a esta función.
  
- **Módulo B (30%)**: Funcionalidad de acceso al catálogo y localización geográfica del almacén. Permite realizar compras de los productos previamente añadidos al carrito de compra de cada usuario.

## Cronograma

- **Tiempo sugerido**: 6 horas de desarrollo.
- **Fecha de entrega recomendada**: 4 de diciembre de 2024.

## Entregables

La entrega final debe incluir dos archivos ZIP:

1. **ZIP Principal (ApellidosNombre_AppSource.zip)**: Contiene el código fuente completo, incluyendo las carpetas `src` y `res`, y el archivo `AndroidManifest.xml`.
  
2. **ZIP Secundario (ApellidosNombre_DocsAndTests.zip)**: Contiene:
   - `README.md` con la descripción del proyecto.
   - Documentación técnica y manual de usuario.
   - Archivos de pruebas y reportes en PDF.

---

**Nota**: La aplicación está diseñada para ser un proyecto colaborativo, integrando elementos de desarrollo móvil y web con enfoque en aplicaciones RESTful, geolocalización, y gestión de productos en tiempo real.
