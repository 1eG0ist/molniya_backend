# Use the official Gradle image to build the application
FROM gradle:8.5-alpine as build

# Copy the source code and build files
COPY src src
COPY build.gradle.kts build.gradle.kts
COPY settings.gradle.kts settings.gradle.kts

# Copy all necessary files
COPY . .

# Build the application with out tests
RUN gradle clean build -x test

ENTRYPOINT ["top", "-b"]

# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Create a non-root user
RUN adduser --system molniyauser && addgroup --system molniyauser && adduser molniyauser molniyauser

# Set the working directory
WORKDIR /app

# Change ownership of the working directory
RUN chown -R molniyauser:molniyauser /app

# Copy the built JAR file from the build stage
COPY --from=build /home/gradle/build/libs/sportgram_backend-0.0.1-SNAPSHOT.jar ./application.jar

# Copy the init.sh script
COPY init.sh ./init.sh

# Make the init.sh script executable as root
USER root
RUN chmod +x ./init.sh

# Switch back to the non-root user
USER molniyauser

# Expose the port the app runs on
EXPOSE 8080

# Set the command to run the application (using vps profile, not for local dev, decause in vps profile need up with docker-compose)
ENTRYPOINT ["/bin/sh", "-c", "./init.sh && java -jar /app/application.jar --spring.profiles.active=vps"]
