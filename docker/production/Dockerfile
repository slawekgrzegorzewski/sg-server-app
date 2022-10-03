FROM openjdk:17.0.2-jdk
COPY build/libs/accountant-0.0.1-SNAPSHOT.jar /app/accountant.jar
COPY docker/production/logback.xml /app/logback.xml
RUN mkdir -p /app/logs

EXPOSE 8080

CMD ["java", "-jar", "/app/accountant.jar", "--spring.config.location=file:///app/application.yml", "--logging.config=/app/logback.xml"]