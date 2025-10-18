# --- Сборка jar ---
FROM bellsoft/liberica-openjdk-debian:21 AS builder
WORKDIR /app
COPY . .
RUN apt-get update && apt-get install -y maven && \
    mvn -pl notification-service -am clean package -DskipTests

# --- Запуск ---
FROM bellsoft/liberica-openjre-alpine:21
WORKDIR /app
COPY --from=builder /app/notification-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]