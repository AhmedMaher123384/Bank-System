FROM  openjdk:22
LABEL authors="ahmedmaher"
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
