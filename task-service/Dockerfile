FROM openjdk:21-jdk-slim
WORKDIR /app
COPY task-service/build/libs/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]