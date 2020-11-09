FROM openjdk:15.0.1-jdk

COPY ./accountant.jar /app/accountant.jar
COPY ./application.yml /app/application.yml
COPY ./data.sql /app/data.sql

EXPOSE 8080

CMD ["java", "-jar", "/app/accountant.jar", "--spring.config.location=file:///app/application.yml"]