package pl.sg;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.validation.constraints.NotNull;

@Transactional
@ContextConfiguration(initializers = AbstractContainerBaseTest.DockerPostgresDataSourceInitializer.class)
public abstract class AbstractContainerBaseTest {

    static final PostgreSQLContainer POSTGRES;

    @LocalServerPort
    protected int serverPort;
    @Autowired
    protected TestRestTemplate restTemplate;


    static {
        POSTGRES = new PostgreSQLContainer("postgres:14.5").withDatabaseName("accountant").withUsername("postgres");
        POSTGRES.start();

        Flyway flyway = Flyway.configure().dataSource(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword()).load();
        flyway.migrate();
    }

    public AbstractContainerBaseTest() {
//        restTemplate = new TestRestTemplate();
//        restTemplate.getRestTemplate().setRequestFactory(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
//        restTemplate.getRestTemplate().setInterceptors(List.of(
//                new DebugRestTemplateInterceptor()
//        ));
    }

    static class DockerPostgresDataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(applicationContext, "spring.datasource.url=" + POSTGRES.getJdbcUrl(), "spring.datasource.username=" + POSTGRES.getUsername(), "spring.datasource.password=" + POSTGRES.getPassword());
        }
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
