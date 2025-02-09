# Используем свежий образ Maven с OpenJDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
COPY . /app

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app
LABEL authors="Fisher48"
RUN mvn clean package -DskipTests

# Используем eclipse образ OpenJDK для запуска
FROM eclipse-temurin:21-jdk

COPY --from=builder /app/target/VehiclePark-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8888
# CMD ["java","-jar","/app.jar"]

# Используем команду запуска через shell, чтобы не было проблем с интерактивностью
CMD ["sh", "-c", "java -Dspring.shell.interactive.enabled=true -jar /app.jar"]

