package pl.sg.accountant;

import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.repository.ApplicationUserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.constraints.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@ContextConfiguration(initializers = AbstractContainerBaseTest.DockerPostgresDataSourceInitializer.class)
public abstract class AbstractContainerBaseTest {

    @LocalServerPort
    int serverPort;

    @Autowired
    ApplicationUserRepository applicationUserRepository;

    @Autowired
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

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

    void clearApplicationUserRoles() {
        setApplicationUserRoles();
    }

    void setApplicationUserRoles(String... roles) {
        runInTransaction((entityManager) -> {
            ApplicationUser applicationUser = entityManager.getReference(ApplicationUser.class, 1);
            applicationUser.setRoles(roles);
            entityManager.persist(applicationUser);
        });
    }

    void runInTransaction(Consumer<EntityManager> action) {
        callInTransaction(em -> {
            action.accept(em);
            return null;
        });
    }

    <T> T callInTransaction(Function<EntityManager, T> action) {
        if (entityManager == null) {
            entityManager = entityManagerFactory.createEntityManager();
        }
        try {
            entityManager.getTransaction().begin();
            T result = action.apply(entityManager);
            entityManager.getTransaction().commit();
            return result;
        } catch (Exception ex) {
            entityManager.getTransaction().rollback();
            throw ex;
        }
    }

    @org.jetbrains.annotations.NotNull
    Response post(int domainId, String endpoint, ThrowingConsumer<HttpPost> configurer) throws Exception {
        return request(HttpPost::new, domainId, endpoint, configurer);
    }

    @org.jetbrains.annotations.NotNull
    Response get(int domainId, String endpoint) throws Exception {
        return request(HttpGet::new, domainId, endpoint);
    }

    @org.jetbrains.annotations.NotNull
    Response put(int domainId, String endpoint, ThrowingConsumer<HttpPut> configurer) throws Exception {
        return request(HttpPut::new, domainId, endpoint, configurer);
    }

    @org.jetbrains.annotations.NotNull
    private <T extends HttpRequestBase> Response request(Function<String, T> constructor,
                                                         int domainId,
                                                         String endpoint) throws Exception {
        return request(constructor, domainId, endpoint, t -> {
        });
    }

    @org.jetbrains.annotations.NotNull
    private <T extends HttpRequestBase> Response request(Function<String, T> constructor,
                                                         int domainId,
                                                         String endpoint,
                                                         ThrowingConsumer<T> configurer) throws Exception {
        String token = login();
        T request = constructor.apply("http://localhost:" + serverPort + endpoint);
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader("domainId", String.valueOf(domainId));
        request.setHeader("Authorization", "Bearer " + token);
        configurer.accept(request);
        try (CloseableHttpClient client = HttpClientBuilder.create().build();
             CloseableHttpResponse response = client.execute(request);
             InputStream content = response.getEntity().getContent();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(content))) {
            return new Response(
                    response.getStatusLine().getStatusCode(),
                    bufferedReader.lines().collect(Collectors.joining()));
        }
    }

    public interface ThrowingConsumer<T> {

        void accept(T t) throws Exception;

        default ThrowingConsumer<T> andThen(Consumer<? super T> after) throws Exception {
            Objects.requireNonNull(after);
            return (T t) -> {
                accept(t);
                after.accept(t);
            };
        }
    }

    public class Response {
        private final int code;
        private final String body;

        public Response(int code, String body) {
            this.code = code;
            this.body = body;
        }

        public int code() {
            return code;
        }

        public String body() {
            return body;
        }
    }
}
