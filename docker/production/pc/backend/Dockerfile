FROM openjdk:15.0.1-jdk

COPY ./accountant.jar /app/accountant.jar
COPY ./application.yml /app/application.yml
COPY ./data.sql /app/data.sql

RUN echo "" > /app/log.txt && echo "" > /app/error.txt && chmod 777 /app/log.txt && chmod 777 /app/error.txt
EXPOSE 8080

CMD ["java", "-jar", "/app/accountant.jar", "--spring.config.location=file:///app/application.yml", ">/app/log.txt", "2>/app/error.txt"]