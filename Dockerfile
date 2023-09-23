FROM openjdk:17-jdk-alpine

EXPOSE 8080
COPY target/CloudServiceDiplom-0.0.1-SNAPSHOT.jar netology-diplom-backend.jar
CMD ["java", "-jar", "netology-diplom-backend.jar"]