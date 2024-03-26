FROM azul/zulu-openjdk-debian:21
COPY build/libs/sg-server-app-0.0.1-SNAPSHOT.jar /app/accountant.jar
RUN mkdir -p /app/logs

EXPOSE 8080

CMD ["java", "-jar", "/app/accountant.jar", "--spring.config.location=file:///app/application.yml", "--logging.config=/app/logback.xml"]