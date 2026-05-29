FROM eclipse-temurin:8-jdk

COPY target/*.jar app.jar

EXPOSE 9090

ENTRYPOINT ["java", "-jar", "app.jar"]