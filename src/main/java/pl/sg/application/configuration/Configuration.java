package pl.sg.application.configuration;

import org.springframework.stereotype.Component;

@Component
public class Configuration {
    public String getJWTTokenSecret() {
        return getEnv("JWT_SECRET", "confidentialsecret");
    }

    private String getEnv(String key, String defaultValue) {
        String value = System.getenv(key);
        return value == null ? defaultValue : value;
    }
}
