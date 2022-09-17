package pl.sg.application.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class Configuration {

    private static final Logger LOG = LoggerFactory.getLogger(Configuration.class);

    public String getJWTTokenSecret() {
        return getEnv("JWT_SECRET", Paths.get("/", "run", "secrets", "jwt_secret_code"), "confidentialsecret");
    }
    public String getSendgridApiKey() {
        return getEnv("SENDGRID_API_KEY", Paths.get("/", "run", "secrets", "sendgrid_api_key"), "");
    }
    public String getNordigenSecretId() {
        return getEnv("NORDIGEN_SECRET_ID", Paths.get("/", "run", "secrets", "nordigen_secret_id"), "");
    }
    public String getNordigenSecretKey() {
        return getEnv("NORDIGEN_SECRET_KEY", Paths.get("/", "run", "secrets", "nordigen_secret_key"), "");
    }

    private String getEnv(String key, Path secretPath, String defaultValue) {
        String value = System.getenv(key);
        if (value == null) {
            try {
                value = Files.readString(secretPath);
            } catch (IOException e) {
                value = null;
                LOG.warn("Problem during reading secret " + secretPath + " from a file, using default value.", e);
            }
        }
        return value == null ? defaultValue : value;
    }
}
