FROM openjdk:8
ADD target/newsletter-service-*.jar newsletter-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "newsletter-service.jar"]