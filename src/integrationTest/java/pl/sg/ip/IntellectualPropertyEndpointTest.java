package pl.sg.ip;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.sg.ip.api.IntellectualPropertyData;
import pl.sg.ip.model.IntellectualProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@Testcontainers
public class IntellectualPropertyEndpointTest extends AbstractIPBaseTest {

    private static final String INTELLECTUAL_PROPERTY_DESCRIPTION = "description";
    private static final int SECOND_DOMAIN_ID = 2;

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    public void getIPRCheckRolesAccess(String[] roles, int expectedResponse) {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, roles);
        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                String.class);
        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    public void createIPRCheckRolesAccess(String[] roles, int expectedResponse) throws Exception {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, roles);
        headers.setContentType(MediaType.APPLICATION_JSON);

        IntellectualPropertyData testIPRToCreate = new IntellectualPropertyData(
                LocalDate.now().minusDays(1),
                LocalDate.now(),
                INTELLECTUAL_PROPERTY_DESCRIPTION);

        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testIPRToCreate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.PUT,
                requestEntity,
                String.class);

        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    public void updateIPRCheckRolesAccess(String[] roles, int expectedResponse) throws Exception {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, roles);
        headers.setContentType(MediaType.APPLICATION_JSON);

        IntellectualPropertyData testIPRToUpdate = new IntellectualPropertyData(
                LocalDate.now().minusDays(1),
                LocalDate.now(),
                INTELLECTUAL_PROPERTY_DESCRIPTION);

        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testIPRToUpdate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/1",
                HttpMethod.PATCH,
                requestEntity,
                String.class);

        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    public void deleteIPRCheckRolesAccess(String[] roles, int expectedResponse) {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, roles);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/1",
                HttpMethod.DELETE,
                requestEntity,
                String.class);

        assertEquals(expectedResponse, response.getStatusCode().value());
    }

    @Test
    public void shouldGetDomainOnlyData() throws JsonProcessingException {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);

        createIntellectualPropertyWithTaskAndTimeRecords(DEFAULT_DOMAIN_ID, startDate, endDate, INTELLECTUAL_PROPERTY_DESCRIPTION);
        createIntellectualPropertyWithTaskAndTimeRecords(SECOND_DOMAIN_ID, startDate, startDate.minusDays(1), INTELLECTUAL_PROPERTY_DESCRIPTION);

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, "IPR");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.GET,
                new HttpEntity<String>(headers),
                String.class);

        Object body = response.getBody();

        List<pl.sg.ip.api.IntellectualProperty> intellectualProperties = objectMapper.readValue(Objects.requireNonNull(body).toString(), new TypeReference<>() {
        });
        assertEquals(1, intellectualProperties.size());
        assertEquals(INTELLECTUAL_PROPERTY_DESCRIPTION, intellectualProperties.get(0).getDescription());
        assertEquals(startDate, intellectualProperties.get(0).getStartDate());
        assertEquals(endDate, intellectualProperties.get(0).getEndDate());
        assertEquals(DEFAULT_DOMAIN_ID, intellectualProperties.get(0).getDomain().getId());
    }

    @Test
    public void shouldCreateIntellectualProperty() throws JsonProcessingException {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);

        IntellectualPropertyData testIPRToCreate = new IntellectualPropertyData(
                startDate,
                endDate,
                INTELLECTUAL_PROPERTY_DESCRIPTION);

        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testIPRToCreate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.PUT,
                requestEntity,
                String.class);

        List<IntellectualProperty> dbValue = intellectualPropertyRepository.findAll();
        assertEquals(1, dbValue.size());
        assertEquals(INTELLECTUAL_PROPERTY_DESCRIPTION, dbValue.get(0).getDescription());
        assertEquals(startDate, dbValue.get(0).getStartDate());
        assertEquals(endDate, dbValue.get(0).getEndDate());
        assertEquals(DEFAULT_DOMAIN_ID, dbValue.get(0).getDomain().getId());

        pl.sg.ip.api.IntellectualProperty fromResponse = objectMapper.readValue(Objects.requireNonNull(response.getBody()).toString(), pl.sg.ip.api.IntellectualProperty.class);
        assertEquals(dbValue.get(0).getId(), fromResponse.getId());
        assertEquals(INTELLECTUAL_PROPERTY_DESCRIPTION, fromResponse.getDescription());
        assertEquals(startDate, fromResponse.getStartDate());
        assertEquals(endDate, fromResponse.getEndDate());
        assertEquals(DEFAULT_DOMAIN_ID, fromResponse.getDomain().getId());
    }

    @Test
    public void shouldFailUpdateWhenEntityDoesntExist() throws JsonProcessingException {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);

        IntellectualPropertyData testIPRToUpdate = new IntellectualPropertyData(
                startDate,
                endDate,
                INTELLECTUAL_PROPERTY_DESCRIPTION);

        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(testIPRToUpdate), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/1",
                HttpMethod.PATCH,
                requestEntity,
                String.class);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void shouldFailUpdateForOtherDomain() throws JsonProcessingException {

        String newDescription = "newDescription";

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);

        IntellectualProperty testIPRToUpdate = createIntellectualPropertyWithTaskAndTimeRecords(SECOND_DOMAIN_ID, startDate, endDate, INTELLECTUAL_PROPERTY_DESCRIPTION);
        tryToUpdateAndExceptResponseCode(testIPRToUpdate.getId(), new IntellectualPropertyData(startDate, endDate, newDescription), 403);
    }

    @Test
    public void shouldUpdate() throws JsonProcessingException {

        String newDescription = "newDescription";

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);

        IntellectualProperty testIPRToUpdate = createIntellectualPropertyWithTaskAndTimeRecords(DEFAULT_DOMAIN_ID, startDate, endDate, INTELLECTUAL_PROPERTY_DESCRIPTION);

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);
        LocalDate newStartDate = startDate.minusDays(1);
        LocalDate newEndDate = endDate.plusDays(1);
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(new IntellectualPropertyData(newStartDate, newEndDate, newDescription)), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + testIPRToUpdate.getId(),
                HttpMethod.PATCH,
                requestEntity,
                String.class);

        assertEquals(200, response.getStatusCode().value());
        IntellectualProperty updated = intellectualPropertyRepository.getReferenceById(testIPRToUpdate.getId());
        assertEquals(newDescription, updated.getDescription());
        assertEquals(newStartDate, updated.getStartDate());
        assertEquals(newEndDate, updated.getEndDate());
    }

    @Test
    public void shouldFailUpdateWhenUpdatingDatesOutsideOfTimeRecordsBoundaries() throws JsonProcessingException {

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(1);

        IntellectualProperty testIPRToUpdate = createIntellectualPropertyWithTaskAndTimeRecords(DEFAULT_DOMAIN_ID, startDate, endDate, INTELLECTUAL_PROPERTY_DESCRIPTION);
        tryToUpdateAndExceptResponseCode(testIPRToUpdate.getId(), new IntellectualPropertyData(startDate.plusDays(1), endDate, INTELLECTUAL_PROPERTY_DESCRIPTION), 400);
        tryToUpdateAndExceptResponseCode(testIPRToUpdate.getId(), new IntellectualPropertyData(startDate, endDate.minusDays(1), INTELLECTUAL_PROPERTY_DESCRIPTION), 400);
    }

    private void tryToUpdateAndExceptResponseCode(int ipId, IntellectualPropertyData updateInfo, int responseCode) throws JsonProcessingException {
        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestEntity = new HttpEntity<>(objectMapper.writeValueAsString(updateInfo), headers);

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + ipId,
                HttpMethod.PATCH,
                requestEntity,
                String.class);

        assertEquals(responseCode, response.getStatusCode().value());
    }

    @Test
    public void shouldDeleteIntellectualProperty() {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, "IPR");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        IntellectualProperty intellectualProperty = createIntellectualProperty(DEFAULT_DOMAIN_ID, LocalDate.now().minusDays(1), LocalDate.now(), "");

        restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + intellectualProperty.getId(),
                HttpMethod.DELETE,
                requestEntity,
                String.class);

        assertFalse(intellectualPropertyRepository.existsById(intellectualProperty.getId()));
    }

    @Test
    public void shouldFailDeletionIntellectualPropertyContainsAnyDependantEntities() {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, "IPR");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        IntellectualProperty intellectualProperty = createIntellectualPropertyWithTaskAndTimeRecords(DEFAULT_DOMAIN_ID, LocalDate.now().minusDays(1), LocalDate.now(), "");

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + intellectualProperty.getId(),
                HttpMethod.DELETE,
                requestEntity,
                String.class);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(intellectualPropertyRepository.existsById(intellectualProperty.getId()));
    }

    @Test
    public void shouldFailDeletionIntellectualPropertyFromOtherDomain() {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, "IPR");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        IntellectualProperty intellectualProperty = createIntellectualProperty(SECOND_DOMAIN_ID, LocalDate.now().minusDays(1), LocalDate.now(), "");

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + intellectualProperty.getId(),
                HttpMethod.DELETE,
                requestEntity,
                String.class);

        assertEquals(403, response.getStatusCode().value());
        assertTrue(intellectualPropertyRepository.existsById(intellectualProperty.getId()));
    }

    @Test
    public void shouldFailDeletionIntellectualPropertyWhenEntityDoesntExist() {

        HttpHeaders headers = headers(DEFAULT_DOMAIN_ID, "IPR");

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        IntellectualProperty intellectualProperty = createIntellectualProperty(SECOND_DOMAIN_ID, LocalDate.now().minusDays(1), LocalDate.now(), "");

        ResponseEntity<?> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + (intellectualProperty.getId() + 1),
                HttpMethod.DELETE,
                requestEntity,
                String.class);

        assertEquals(404, response.getStatusCode().value());
        assertTrue(intellectualPropertyRepository.existsById(intellectualProperty.getId()));
    }
}