# Guía de Instalación y Uso del Sistema

**Autores**: Sergio Hervás Cobo y Adriano García Giralda
**Practica 3** de la asignatura **Desarrollo de Sistemas de Software Basados en Componentes y Servicios** en el **Máster en Ingeniería Informática** de la **UGR**
## Descripción

Este sistema consta de una aplicación móvil desarrollada en Android Studio utilizando Kotlin y un backend implementado con Spring Boot, diseñado para proporcionar funcionalidades de visualización de productos, gestión del carrito de compras, y administración de productos. El backend también ofrece una API para la comunicación con la aplicación móvil y gestiona las operaciones en la base de datos del servidor.

## Instrucciones de Instalación y Ejecución

### Instalación del Backend

1. **Descargar el archivo ZIP** correspondiente:
   - El código del servidor se encuentra en el archivo ZIP de la entrega de la práctica 1.
   - El código de la aplicación Android se encuentra en el archivo ZIP de la entrega de la práctica 3 (P3).

2. **Extraer los archivos** en directorios separados.

3. **Abrir el proyecto de la práctica 1 en Spring Tool Suite (STS)**.

4. **Realizar la compilación de Maven**:
   Ejecutar el siguiente comando sobre el archivo `pom.xml`:
   ```bash
   mvn clean install

5. **Ejecutar el backend:**
Inicie el backend ejecutando la clase `Application11` (la clase principal que contiene el método `main`).
Haga clic derecho sobre la clase `Application11` y seleccione `Run As > Spring Boot App`.

6. **Verificación:**
Acceda al backend a través de su navegador ingresando `http://localhost:8080` para verificar que el servidor esté corriendo correctamente.

## Instalación de la Aplicación Móvil

1. **Abrir el proyecto en Android Studio.**

2. **Configurar la dirección IP:**
   En el archivo `Constants`, actualice la dirección IP según el tipo de dispositivo que esté utilizando:
   - Si está utilizando un dispositivo físico conectado por USB, agregue la dirección IP de su máquina en la red local.
   - Si está utilizando el emulador de Android Studio, utilice la dirección IP `10.0.2.2`, que es la dirección predeterminada para acceder al host desde el emulador.

3. **Configurar la red:**
   En el archivo `res/xml/network_security_config`, permita la dirección IP añadida en el paso anterior, asegurándose de que la aplicación pueda comunicarse correctamente con el backend.

4. **Ejecutar la aplicación:**
   Haga clic en el botón `Run` o presione `Shift + F10` para iniciar la aplicación en el dispositivo/emulador.

## Dependencias Usadas

- **Retrofit:** Para la comunicación HTTP entre la aplicación móvil y el backend.
- **Gson:** Para la serialización y deserialización de datos JSON.
- **OpenStreetMap API:** Para la visualización de mapas y almacenes cercanos.


## Estructura de Carpetas y Organización del Código

- **backend/:** Contiene el código del servidor desarrollado con Spring Boot.
  - `src/main/java/`: Contiene las clases Java del backend.
  - `src/main/resources/`: Archivos de configuración del servidor, como `application.properties`.

- **android/:** Contiene el código de la aplicación móvil desarrollada con Kotlin en Android Studio.
  - `app/src/main/java/`: Contiene las clases Kotlin de la aplicación.
  - `app/src/main/res/`: Archivos de recursos, como layouts y configuraciones de red.

## Endpoints API Utilizados

1. **GET /api/products**
   - **Descripción:** Obtiene la lista de todos los productos disponibles en la tienda.
   - **Respuesta:** Lista de productos en formato JSON.

2. **POST /api/products**
   - **Descripción:** Crea un nuevo producto en la base de datos.
   - **Cuerpo de la solicitud:** Detalles del producto (nombre, descripción, precio, etc.)
   - **Respuesta:** Confirmación de la creación del producto.

3. **DELETE /api/admin/delete_product/{id}**
   - **Descripción:** Elimina un producto de la base de datos.
   - **Parámetros:** id del producto a eliminar.
   - **Respuesta:** Confirmación de la eliminación del producto.

4. **POST /api/order**
   - **Descripción:** Registra un pedido en la base de datos del servidor.
   - **Cuerpo de la solicitud:** Detalles del pedido (productos, cantidades, etc.)
   - **Respuesta:** Confirmación del registro del pedido.

5. **POST /api/login**
   - **Descripción:** Loguearse en el servidor.
   - **Cuerpo de la solicitud:** Credenciales del usuario.
   - **Respuesta:** Confirmación del login con el token de sesión en caso satisfactorio.

6. **GET /verify-session**
    - **Descripción:** Verifica la sesión del usuario a partir de su `sessionId` en las cookies.
    - **Parámetros:** 
      - `sessionId`: El ID de la sesión proporcionado en las cookies.
    - **Respuesta:** `SessionVerificationResponse`, que contiene la verificación del estado de la sesión.