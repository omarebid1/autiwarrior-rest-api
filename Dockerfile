# Use a slimmer, stable Java 19 base image
FROM eclipse-temurin:19-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file
COPY target/autiwarrior-0.0.1-SNAPSHOT.jar autiwarrior.jar

# Label the maintainer (optional but good practice)
LABEL maintainer="Omar Ebid"

# Expose the port used by Spring Boot
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "autiwarrior.jar"]
