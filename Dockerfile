FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

COPY src/main/resources/db/migration ./db/migration

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]