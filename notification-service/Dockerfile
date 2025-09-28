FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn -pl notification-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/notification-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]