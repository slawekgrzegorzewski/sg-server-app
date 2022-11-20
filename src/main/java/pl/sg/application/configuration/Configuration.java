package pl.sg.application.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.util.Optional.*;

@Component
public class Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    public String getJWTTokenSecret() {
        return getEnv("JWT_SECRET", Paths.get("/", "run", "secrets", "jwt_secret_code")).orElse("confidentialsecret");
    }

    public String getSendgridApiKey() {
        return getEnv("SENDGRID_API_KEY", Paths.get("/", "run", "secrets", "sendgrid_api_key")).orElse("");
    }

    public String getNordigenSecretId() {
        return getEnv("NORDIGEN_SECRET_ID", Paths.get("/", "run", "secrets", "nordigen_secret_id")).orElse("");
    }

    public String getNordigenSecretKey() {
        return getEnv("NORDIGEN_SECRET_KEY", Paths.get("/", "run", "secrets", "nordigen_secret_key")).orElse("");
    }

    public String getAWSAccessKeyId() {
        return getEnv("AWS_ACCESS_KEY_ID", Paths.get("/", "run", "secrets", "aws_access_key_id")).orElseThrow();
    }

    public String getAWSSecretAccessKey() {
        return getEnv("AWS_SECRET_ACCESS_KEY", Paths.get("/", "run", "secrets", "aws_secret_access_key")).orElseThrow();
    }

    public String getAWSRegion() {
        return getEnv("AWS_REGION", Paths.get("/", "run", "secrets", "aws_region")).orElseThrow();
    }

    private Optional<String> getEnv(String key, Path secretPath) {
        return ofNullable(System.getenv(key)).or(() -> getSecret(secretPath));
    }

    private Optional<String> getSecret(Path secretPath) {
        try {
            return of(Files.readString(secretPath));
        } catch (IOException e) {
            LOG.warn("Problem during reading secret file.", e);
            return empty();
        }
    }
}
