package pl.sg.accountant;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.flywaydb.core.Flyway;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@ContextConfiguration(initializers = AbstractContainerBaseTest.DockerPostgresDataSourceInitializer.class)
public abstract class AbstractContainerBaseTest {

    @LocalServerPort
    int serverPort;

    static final PostgreSQLContainer POSTGRES;

    static {
        POSTGRES = new PostgreSQLContainer("postgres:14.5").withDatabaseName("accountant").withUsername("postgres");
        POSTGRES.start();

        Flyway flyway = Flyway.configure().dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword()).load();
        flyway.migrate();
    }

    static class DockerPostgresDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "spring.datasource.url=" + POSTGRES.getJdbcUrl(), "spring.datasource.username=" + POSTGRES.getUsername(), "spring.datasource.password=" + POSTGRES.getPassword());
        }
    }

    String login() throws IOException {
        HttpPost request = new HttpPost("http://localhost:" + serverPort + "/login");
        request.setEntity(new StringEntity("slawek"));

        try (CloseableHttpClient client = HttpClientBuilder.create().build();
             InputStream content = client.execute(request).getEntity().getContent();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content))) {
            return bufferedReader.lines().collect(Collectors.joining());
        }
    }
}
