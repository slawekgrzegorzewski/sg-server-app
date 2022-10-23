package pl.sg.accountant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.sg.application.model.Domain;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ipr.model.IntellectualProperty;
import pl.sg.ipr.repository.IntellectualPropertyRepository;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@Testcontainers
public class IprTest extends AbstractContainerBaseTest {

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    IntellectualPropertyRepository intellectualPropertyRepository;

    public IprTest() {
    }

    @Test
    public void test() {
        Domain a = domainRepository.findById(1)
                .orElseGet(() -> domainRepository.save(new Domain(1, "a")));
        intellectualPropertyRepository.save(
                new IntellectualProperty(
                        LocalDate.now(),
                        LocalDate.now(),
                        "a",
                        a
                )
        );
        System.out.println();
    }
}