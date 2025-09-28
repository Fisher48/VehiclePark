FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY . .
RUN mvn -pl telegram-bot-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=builder /app/telegram-bot-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]