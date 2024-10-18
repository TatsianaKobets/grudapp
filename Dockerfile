FROM openjdk:17-alpine
# Установить Maven
RUN apk add --no-cache maven
# Set the working directory to /app
WORKDIR /app
RUN apk add --no-cache postgresql-libs
# Copy the pom.xml file into the working directory
#COPY pom.xml .
COPY . .
#COPY lib/postgresql-42.7.2.jar /app/lib/
# Copy the source code into the working directory
#COPY src/main/java/ org/example/grudapp/dbconnect/

# Copy the resources into the working directory
#COPY src/main/resources/ .
RUN mvn package -Dmaven.test.skip=true
# Build the project using Maven
#RUN mvn clean package

# Copy the JAR file into the target directory
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} /app/target/

# Expose the port that the application will use
#EXPOSE 8080

# Set the environment variables
#ENV DB_URL=jdbc:postgresql://localhost:5432/mydb
#ENV DB_USERNAME=user
#ENV DB_PASSWORD=password
CMD ["mvn", "liquibase:update", "-Pdocker"]
# Run the application when the container starts
#ENTRYPOINT ["java", "-jar", "/app/target/grudapp-1.0-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-jar", "/app/target/grudapp-1.0-SNAPSHOT.jar"]
CMD ["java", "-jar", "target/grudapp-1.0-SNAPSHOT.jar"]