# --- Сборка jar ---
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn -pl VehiclePark-core -am clean package -DskipTests

# --- Запуск ---
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/VehiclePark-core/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]