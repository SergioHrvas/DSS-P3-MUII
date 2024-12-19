======================================
============= PRÁCTICA 1 =============
======================================

> Sergio Hervás Cobo
> Desarrollo de Sistemas de Software Basados en Componentes y Servicios
> MII UGR 24/25

Consideraciones importantes sobre la práctica:

1. Contenido extra
   - Filtro para buscar productos por precio
   - Búsqueda de productos por nombre
   - Posibilidad de agregar más de 1 producto al carro.
   - Posibilidad de borrar más de 1 producto del carro.
   - Generar factura del carrito al pagar (no sé si es o no extra, pero no está en el guión y lo he hecho)
   - Página de error personalizada

2. Aunque no era necesario para el problema planteado, he creado la clase Cart por facilitar escalabilidad en un futuro (teniendo en cuenta que si la aplicación se
   ampliase/mejorase, un usuario podría tener varios carros, o un carro podría ser compartido por varios usuarios...)
   El carrito de compra se asocia al usuario cuando este se crea, excepto cuando se hace desde la consola de h2.

3. He utilizado la misma versión de Spring Boot que el proyecto de ejemplo subido a Prado.

4. Se crean automaticamente el usuario admin con contraseña admin123 y user con contraseña user123. Aun asi, hay funcionalidad para que un usuario se registre.

Repositorio de Github: https://github.com/SergioHrvas/DSS-MII-24-25 (si lo necesita y no puede verlo, avíseme y le añado como colaborador)

======================================