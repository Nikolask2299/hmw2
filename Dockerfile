FROM openjdk:21-jdk-slim

COPY target/hmw2-user-1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]