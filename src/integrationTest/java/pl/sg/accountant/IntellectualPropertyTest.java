package pl.sg.accountant;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.sg.ipr.model.IntellectualProperty;
import pl.sg.ipr.repository.IntellectualPropertyRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@Testcontainers
public class IntellectualPropertyTest extends AbstractContainerBaseTest {

    @Autowired
    IntellectualPropertyRepository intellectualPropertyRepository;

    public IntellectualPropertyTest() {
    }

    @Test
    public void test() throws IOException {
        String token = login();
        HttpPut request = new HttpPut("http://localhost:" + serverPort + "/ipr");
        request.setEntity(new StringEntity("""
                {
                "startDate": "%s",
                "endDate": "%s",
                "description": "description"
                }""".formatted(LocalDate.now().minusDays(1), LocalDate.now())));
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");
        request.setHeader("domainId", "1");
        request.setHeader("Authorization", "Bearer " + token);

        try (CloseableHttpClient client = HttpClientBuilder.create().build()) {
            CloseableHttpResponse response = client.execute(request);
        }
        List<IntellectualProperty> all = intellectualPropertyRepository.findAll();
        System.out.println();
    }
}