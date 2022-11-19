package pl.sg;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import pl.sg.application.service.AuthorizationService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Transactional
@ContextConfiguration(initializers = AbstractContainerBaseTest.DockerPostgresDataSourceInitializer.class)
public abstract class AbstractContainerBaseTest {

    static final PostgreSQLContainer POSTGRES;

    protected static final int DEFAULT_DOMAIN_ID = 1;
    protected static final int USER_ID = 1;
    protected static final String USER_NAME = "slawek";

    @LocalServerPort
    protected int serverPort;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    protected TestRestTemplate restTemplate;


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

    protected HttpHeaders authenticatedHeaders(int domainId, String... roles) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("domainId", String.valueOf(domainId));
        headers.setBearerAuth(authorizationService.generateJWTToken(USER_ID + ":" + USER_NAME, List.of(roles), DEFAULT_DOMAIN_ID));
        return headers;
    }

    protected HttpHeaders headers(int domainId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("domainId", String.valueOf(domainId));
        return headers;
    }

    protected static void rollbackAndStartTransaction() {
        if (TestTransaction.isActive()) {
            TestTransaction.flagForRollback();
            TestTransaction.end();
        }
        TestTransaction.start();
    }

    protected static void commitAndStartNewTransaction() {
        if (TestTransaction.isActive()) {
            TestTransaction.flagForCommit();
            TestTransaction.end();
        }
        TestTransaction.start();
    }
}
