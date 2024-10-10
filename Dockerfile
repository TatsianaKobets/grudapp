FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/target/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/target/grudapp-1.0-SNAPSHOT.jar"]