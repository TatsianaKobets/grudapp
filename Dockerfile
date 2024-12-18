FROM maven:3.8.4-openjdk-17-slim AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim

COPY --from=build /app/target/grudapp-1.0-SNAPSHOT.jar /app/grudapp.jar

ENTRYPOINT ["java", "-jar", "/app/grudapp.jar"]