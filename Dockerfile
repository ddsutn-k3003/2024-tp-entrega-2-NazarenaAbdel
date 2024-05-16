# Define la imagen base para la etapa de construcción
FROM maven:3.8.6-openjdk-18 AS build

# Establece el directorio de trabajo en la imagen del contenedor
WORKDIR /app

# Copia solo el archivo POM para evitar copiar todo el contexto
COPY pom.xml .

# Descarga las dependencias necesarias
RUN mvn dependency:go-offline

# Copia el resto del código fuente
COPY src ./src

# Compila y empaqueta la aplicación
RUN mvn package -DskipTests

# Define la imagen base para la etapa de empaquetado
FROM openjdk:17-jdk-slim

# Copia el archivo JAR generado desde la etapa de construcción
COPY --from=build /app/target/javalin-deploy-1.0-SNAPSHOT-jar-with-dependencies.jar /app/logistica.jar

# Expone el puerto en el que la aplicación escuchará las solicitudes entrantes
EXPOSE 8080

# Define el comando de inicio para ejecutar la aplicación
ENTRYPOINT ["java", "-classpath", "/app/logistica.jar", "ar.edu.utn.dds.k3003.app.WebApp"]
