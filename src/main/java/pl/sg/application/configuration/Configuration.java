package pl.sg.application.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.*;

@Component
public class Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);
    private final Environment environment;

    public Configuration(Environment environment) {
        this.environment = environment;
    }

    public String getJWTTokenSecret() {
        return getEnvironmentValue("JWT_SECRET")
                .or(() -> getSecret(Paths.get("/", "run", "secrets", "jwt_secret_code")))
                .orElse("confidentialsecret");
    }

    public String getSendgridApiKey() {
        return getEnvironmentValue("SENDGRID_API_KEY")
                .or(() -> getSecret(Paths.get("/", "run", "secrets", "sendgrid_api_key")))
                .orElse("");
    }

    public String getNordigenSecretId() {
        return getEnvironmentValue("NORDIGEN_SECRET_ID")
                .or(() -> getSecret(Paths.get("/", "run", "secrets", "nordigen_secret_id")))
                .orElse("");
    }

    public String getNordigenSecretKey() {
        return getEnvironmentValue("NORDIGEN_SECRET_KEY")
                .or(() -> getSecret(Paths.get("/", "run", "secrets", "nordigen_secret_key")))
                .orElse("");
    }

    public String getAWSAccessKeyId() {
        return getEnvironmentValue("AWS_ACCESS_KEY_ID")
                .or(() -> getSecret(Paths.get("/", "run", "secrets", "aws_access_key_id")))
                .or(() -> getApplicationConfig("aws.aws_access_key_id"))
                .orElseThrow();
    }

    public String getAWSSecretAccessKey() {
        return getEnvironmentValue("AWS_SECRET_ACCESS_KEY")
                .or(() -> getSecret(Paths.get("/", "run", "secrets", "aws_secret_access_key")))
                .or(() -> getApplicationConfig("aws.aws_secret_access_key"))
                .orElseThrow();
    }

    public String getAWSRegion() {
        return getEnvironmentValue("AWS_REGION")
                .or(() -> getSecret(Paths.get("/", "run", "secrets", "aws_region")))
                .or(() -> getApplicationConfig("aws.aws_region"))
                .orElseThrow();
    }

    public String getRandomOrgApiKey() {
        return getEnvironmentValue("RANDOM_ORG_API_KEY")
                .or(() -> getSecret(Paths.get("/", "run", "secrets", "random_org_api_key")))
                .orElseThrow();
    }

    private Optional<String> getEnvironmentValue(String environmentKey) {
        return ofNullable(System.getenv(environmentKey));
    }

    private Optional<String> getSecret(Path secretPath) {
        if (!Files.exists(secretPath)) {
            return empty();
        }
        try {
            List<String> lines = Files.readAllLines(secretPath, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                return empty();
            }
            return of(lines.get(0));
        } catch (IOException e) {
            LOG.warn("Problem during reading secret file.", e);
            return empty();
        }
    }

    private Optional<String> getApplicationConfig(String key) {
        return ofNullable(environment.getProperty(key, String.class));
    }
}
