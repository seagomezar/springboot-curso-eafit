# Usa una imagen base con OpenJDK 17
FROM openjdk:17-jdk-slim

# Define la carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo jar generado por Maven (o Gradle) al contenedor
COPY target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot (8080)
EXPOSE 8080

# Ejecuta la aplicación Spring Boot con variables de entorno
ENTRYPOINT ["java", "-jar", "app.jar"]