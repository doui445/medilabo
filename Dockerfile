FROM eclipse-temurin:25-jre-alpine
LABEL authors="manu"
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]