# --- Сборка jar ---
#FROM bellsoft/liberica-openjdk-debian:21 AS builder
#WORKDIR /app
#COPY . .
#RUN apt-get update && apt-get install -y maven && \
#    mvn -B -pl VehiclePark-core -am clean package -DskipTests

# --- Запуск ---
#FROM bellsoft/liberica-openjre-alpine:21
#WORKDIR /app
#COPY  target/VehiclePark-core-*.jar app.jar
#ENTRYPOINT ["java", "-jar", "/app/app.jar"]


# Stage 1: копируем JAR снаружи
FROM alpine:3.20 AS artifacts
COPY VehiclePark-core/target/*.jar /build/

# Stage 2: минимальный рантайм
FROM bellsoft/liberica-openjre-alpine:21
WORKDIR /app
COPY --from=artifacts /build/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]