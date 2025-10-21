# --- Сборка jar ---
#FROM bellsoft/liberica-openjdk-debian:21 AS builder
#WORKDIR /app
#COPY . .
#RUN apt-get update && apt-get install -y maven && \
#    mvn -pl notification-service -am clean package -DskipTests

# --- Запуск ---
#FROM maven:3.9.9-eclipse-temurin-21-alpine
#WORKDIR /app
#COPY target/notification-service-*.jar app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]


# Stage 1: копируем JAR снаружи
FROM alpine:3.20 AS artifacts
COPY notification-service/target/*.jar /build/

# Stage 2: минимальный рантайм
FROM bellsoft/liberica-openjre-alpine:21
WORKDIR /app
COPY --from=artifacts /build/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]