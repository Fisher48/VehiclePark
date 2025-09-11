FROM openjdk:21
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Dspring.shell.interactive.enabled=true", "-jar", "/app.jar"]