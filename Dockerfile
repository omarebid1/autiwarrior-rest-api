FROM openjdk:19-jdk

WORKDIR /app

COPY target/autiwarrior-0.0.1-SNAPSHOT.jar /app/autiwarrior.jar

LABEL authors="Omar Ebid"

EXPOSE 8080

CMD ["java" , "-jar" , "autiwarrior.jar"]