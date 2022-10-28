package pl.sg.accountant;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.transaction.TestTransaction;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ipr.api.IntellectualPropertyCreateData;
import pl.sg.ipr.model.IntellectualProperty;
import pl.sg.ipr.repository.IntellectualPropertyRepository;
import pl.sg.ipr.service.IntellectualPropertyService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@Testcontainers
public class IntellectualPropertyTest extends AbstractContainerBaseTest {

    private static final String INTELLECTUAL_PROPERTY_DESCRIPTION = "description";
    private static final int DOMAIN_ID = 1;
    private static final int SECOND_DOMAIN_ID = 2;

    @Autowired
    DomainRepository domainRepository;
    @Autowired
    IntellectualPropertyService intellectualPropertyService;
    @Autowired
    IntellectualPropertyRepository intellectualPropertyRepository;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        if (TestTransaction.isActive()) {
            TestTransaction.flagForRollback();
            TestTransaction.end();
        }
        TestTransaction.start();
        intellectualPropertyRepository.deleteAll();
        TestTransaction.flagForCommit();
        TestTransaction.end();
        TestTransaction.start();
    }

    @ParameterizedTest
    @MethodSource("rolesAndResponses")
    public void shouldFailGettingWhenUserHasNoIPRRole(String[] roles, int expectedResponse) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("domainId", String.valueOf(DOMAIN_ID));
        headers.setBearerAuth(login(roles));
        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                String.class);
        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("rolesAndResponses")
    public void shouldFailCreationWhenUserHasNotIPRRole(String[] roles, int expectedResponse) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("domainId", String.valueOf(DOMAIN_ID));
        headers.setBearerAuth(login(roles));

        IntellectualPropertyCreateData testIPRToCreate = new IntellectualPropertyCreateData(
                LocalDate.now().minusDays(1),
                LocalDate.now(),
                "description");

        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testIPRToCreate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.PUT,
                requestEntity,
                String.class);

        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    private static Stream<Arguments> rolesAndResponses() {
        return Stream.of(
                Arguments.of(new String[]{"ACCOUNTANT_ADMIN"}, 403),
                Arguments.of(new String[]{"ACCOUNTANT_USER"}, 403),
                Arguments.of(new String[]{"CHECKER_ADMIN"}, 403),
                Arguments.of(new String[]{"CHECKER_USER"}, 403),
                Arguments.of(new String[]{"SYR_USER"}, 403),
                Arguments.of(new String[]{"SYR_ADMIN"}, 403),
                Arguments.of(new String[]{"CUBES"}, 403),
                Arguments.of(new String[]{"IPR"}, 200)
        );
    }

    @Test
    public void shouldGetDomainOnlyData() {
        IntellectualProperty intellectualProperty = new IntellectualProperty();
        intellectualProperty.setDomain(domainRepository.getReferenceById(DOMAIN_ID));
        intellectualProperty.setStartDate(LocalDate.now().minusDays(1));
        intellectualProperty.setEndDate(LocalDate.now());
        intellectualProperty.setDescription("description");
        intellectualPropertyRepository.save(intellectualProperty);
        IntellectualProperty intellectualPropertyForOtherDomain = new IntellectualProperty();
        intellectualPropertyForOtherDomain.setDomain(domainRepository.getReferenceById(SECOND_DOMAIN_ID));
        intellectualPropertyForOtherDomain.setStartDate(LocalDate.now().minusDays(2));
        intellectualPropertyForOtherDomain.setEndDate(LocalDate.now().minusDays(1));
        intellectualPropertyForOtherDomain.setDescription("description2");
        intellectualPropertyRepository.save(intellectualProperty);

        List<IntellectualProperty> intellectualProperties = intellectualPropertyService.getAll(DOMAIN_ID);
        assertEquals(1, intellectualProperties.size());
        assertEquals(intellectualProperty, intellectualProperties.get(0));
    }

    @Test
    public void shouldCreateIntellectualProperty() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);
        IntellectualPropertyCreateData testIPRToCreate = new IntellectualPropertyCreateData(
                startDate,
                endDate,
                INTELLECTUAL_PROPERTY_DESCRIPTION);
        IntellectualProperty created = intellectualPropertyService.create(DOMAIN_ID, testIPRToCreate);

        List<IntellectualProperty> all = intellectualPropertyRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(created, all.get(0));
    }
}