package pl.sg.application.configuration;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import pl.sg.configuration.ConfigurationPropertyGetter;
import pl.sg.configuration.model.ConfigurationProperty;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Component
public class Configuration {

    private final Environment environment;
    private final static Path SECRETS_PATH = Paths.get("/", "run", "secrets");
    private final static ConfigurationPropertyGetter configurationPropertyGetter = new ConfigurationPropertyGetter(
            Map.of(
                    "JWT_SECRET", new ConfigurationProperty("JWT_SECRET", SECRETS_PATH.resolve("jwt_secret_code")),
                    "SENDGRID_API_KEY", new ConfigurationProperty("SENDGRID_API_KEY", SECRETS_PATH.resolve("sendgrid_api_key")),
                    "NORDIGEN_SECRET_ID", new ConfigurationProperty("NORDIGEN_SECRET_ID", SECRETS_PATH.resolve("nordigen_secret_id")),
                    "NORDIGEN_SECRET_KEY", new ConfigurationProperty("NORDIGEN_SECRET_KEY", SECRETS_PATH.resolve("nordigen_secret_key")),
                    "AWS_ACCESS_KEY_ID", new ConfigurationProperty("AWS_ACCESS_KEY_ID", SECRETS_PATH.resolve("aws_access_key_id")),
                    "AWS_SECRET_ACCESS_KEY", new ConfigurationProperty("AWS_SECRET_ACCESS_KEY", SECRETS_PATH.resolve("aws_secret_access_key")),
                    "AWS_REGION", new ConfigurationProperty("AWS_REGION", SECRETS_PATH.resolve("aws_region")),
                    "RANDOM_ORG_API_KEY", new ConfigurationProperty("RANDOM_ORG_API_KEY", SECRETS_PATH.resolve("random_org_api_key"))
            )
    );

    public Configuration(Environment environment) {
        this.environment = environment;
    }

    public String getJWTTokenSecret() {
        return configurationPropertyGetter.getOrDefault("JWT_SECRET", "confidentialsecret");
    }

    public String getSendgridApiKey() {
        return configurationPropertyGetter.getOrDefault("SENDGRID_API_KEY", "");
    }

    public String getNordigenSecretId() {
        return configurationPropertyGetter.getOrDefault("NORDIGEN_SECRET_ID", "");
    }

    public String getNordigenSecretKey() {
        return configurationPropertyGetter.getOrDefault("NORDIGEN_SECRET_KEY", "");
    }

    public String getAWSAccessKeyId() {
        return configurationPropertyGetter
                .get("AWS_ACCESS_KEY_ID")
                .or(() -> getApplicationConfig("aws.aws_access_key_id"))
                .orElseThrow();
    }

    public String getAWSSecretAccessKey() {
        return configurationPropertyGetter
                .get("AWS_SECRET_ACCESS_KEY")
                .or(() -> getApplicationConfig("aws.aws_secret_access_key"))
                .orElseThrow();
    }

    public String getAWSRegion() {
        return configurationPropertyGetter
                .get("AWS_REGION")
                .or(() -> getApplicationConfig("aws.aws_region"))
                .orElseThrow();
    }

    public String getRandomOrgApiKey() {
        return configurationPropertyGetter
                .get("RANDOM_ORG_API_KEY")
                .orElseThrow();
    }

    private Optional<String> getApplicationConfig(String key) {
        return ofNullable(environment.getProperty(key, String.class));
    }
}
