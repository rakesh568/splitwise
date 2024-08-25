# Use an official OpenJDK image that also has Maven for building
FROM openjdk:17-jdk-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and Maven Wrapper script into the container
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Copy the source code into the container
COPY src ./src

# Make the Maven Wrapper script executable
RUN chmod +x ./mvnw

# Build the application using the Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Use the same image to run the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/splitwise-0.0.1-SNAPSHOT.jar /app/splitwise.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/splitwise.jar"]