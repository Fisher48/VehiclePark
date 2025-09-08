# Используем этот Dockerfile для всех сервисов (измените название сервиса)
FROM maven:3.9.6-eclipse-temurin-21 as builder

WORKDIR /app
COPY pom.xml .
COPY VehiclePark-core/pom.xml /VehiclePark-core/pom.xml
COPY notification-service/pom.xml /notification-service/pom.xml
COPY telegram-bot-service/pom.xml /telegram-bot-service/pom.xml

# Скачиваем зависимости
RUN mvn dependency:go-offline -B

# Копируем исходный код
COPY VehiclePark-core/src /VehiclePark-core/src

# Собираем конкретный модуль
RUN mvn package -pl VehiclePark-core -am -DskipTests

FROM eclipse-temurin:21-jre
COPY --from=builder /app/VehiclePark-core/target/*.jar /app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
CMD ["java","-jar","/app.jar"]

## Используем команду запуска через shell, чтобы не было проблем с интерактивностью
#CMD ["sh", "-c", "java -Dspring.shell.interactive.enabled=true -jar /app.jar"]

