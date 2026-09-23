# API REST de discografías

API REST Spring para administración de discografías favoritas de empleados de IPLACEX

## Tecnologías
- Java 21
- Spring Boot
- MongoDB Atlas
- Gradle
- Docker

## Configuración
La variable de entorno MONGODB_URI debe contener la conexión a MongoDB Atlas.
La base de datos utilizada es discografica-db.

## Despliegue
El Dockerfile compila discografia-1.war con Gradle y lo ejecuta con OpenJDK 21.