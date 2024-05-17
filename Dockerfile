# syntax = docker/dockerfile:1.2

#
# Build stage
#
FROM maven:3.8.6-openjdk-17 AS build
COPY . .
RUN mvn clean package assembly:single -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /app/target/javalin-deploy-1.0-SNAPSHOT-jar-with-dependencies.jar logistica.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "logistica.jar"]






