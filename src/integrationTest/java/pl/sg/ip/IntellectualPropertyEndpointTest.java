package pl.sg.ip;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.sg.ip.api.IntellectualPropertyData;
import pl.sg.ip.model.IntellectualProperty;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles({"dev", "fileSystemStorage"})
@Testcontainers
public class IntellectualPropertyEndpointTest extends AbstractIPBaseTest {

    private static final String INTELLECTUAL_PROPERTY_DESCRIPTION = "description";
    private static final int SECOND_DOMAIN_ID = 2;
    public static final LocalDate NOW = LocalDate.now();

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    public void getIPRCheckRolesAccess(String role) {
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, role);
        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Void.class);
        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    public void createIPRCheckRolesAccess(String role) {
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, role);
        headers.setContentType(MediaType.APPLICATION_JSON);

        IntellectualPropertyData testIPRToCreate = new IntellectualPropertyData(INTELLECTUAL_PROPERTY_DESCRIPTION);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.PUT,
                new HttpEntity<>(testIPRToCreate, headers),
                Void.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    public void updateIPRCheckRolesAccess(String role) {
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, role);
        headers.setContentType(MediaType.APPLICATION_JSON);

        IntellectualPropertyData testIPRToUpdate = new IntellectualPropertyData(INTELLECTUAL_PROPERTY_DESCRIPTION);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/1",
                HttpMethod.PATCH,
                new HttpEntity<>(testIPRToUpdate, headers),
                Void.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @ParameterizedTest
    @MethodSource("forbiddenRolesAndResponses")
    public void deleteIPRCheckRolesAccess(String role) {
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, role);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/1",
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    public void shouldGetDomainOnlyData() {
        intellectualPropertyTaskTimeRecords(DEFAULT_DOMAIN_ID, INTELLECTUAL_PROPERTY_DESCRIPTION, NOW);
        intellectualPropertyTaskTimeRecords(SECOND_DOMAIN_ID, INTELLECTUAL_PROPERTY_DESCRIPTION, NOW);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        ResponseEntity<List<pl.sg.ip.api.IntellectualProperty>> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<>() {
                });

        List<pl.sg.ip.api.IntellectualProperty> intellectualProperties = response.getBody();
        assertNotNull(intellectualProperties);
        assertEquals(1, intellectualProperties.size());
        pl.sg.ip.api.IntellectualProperty intellectualProperty = intellectualProperties.get(0);
        assertEquals(INTELLECTUAL_PROPERTY_DESCRIPTION, intellectualProperty.getDescription());
        assertEquals(DEFAULT_DOMAIN_ID, intellectualProperty.getDomain().getId());
        assertEquals(1, intellectualProperty.getTasks().size());
        assertEquals(1, intellectualProperty.getTasks().get(0).getTimeRecords().size());
        assertEquals(NOW, intellectualProperty.getTasks().get(0).getTimeRecords().get(0).getDate());
    }

    @Test
    public void shouldCreateIntellectualProperty() {
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        IntellectualPropertyData testIPRToCreate = new IntellectualPropertyData(INTELLECTUAL_PROPERTY_DESCRIPTION);

        ResponseEntity<pl.sg.ip.api.IntellectualProperty> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr",
                HttpMethod.PUT,
                new HttpEntity<>(testIPRToCreate, headers),
                pl.sg.ip.api.IntellectualProperty.class);

        List<IntellectualProperty> dbValue = intellectualPropertyRepository.findAll();
        assertEquals(1, dbValue.size());
        assertEquals(INTELLECTUAL_PROPERTY_DESCRIPTION, dbValue.get(0).getDescription());
        assertEquals(DEFAULT_DOMAIN_ID, dbValue.get(0).getDomain().getId());

        pl.sg.ip.api.IntellectualProperty fromResponse = response.getBody();
        assertEquals(dbValue.get(0).getId(), fromResponse.getId());
        assertEquals(INTELLECTUAL_PROPERTY_DESCRIPTION, fromResponse.getDescription());
        assertEquals(DEFAULT_DOMAIN_ID, fromResponse.getDomain().getId());
    }

    @Test
    public void shouldFailUpdateWhenEntityDoesntExist() {

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/1",
                HttpMethod.PATCH,
                new HttpEntity<>(new IntellectualPropertyData(INTELLECTUAL_PROPERTY_DESCRIPTION), headers),
                Void.class);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    public void shouldFailUpdateForOtherDomain() {
        String newDescription = "newDescription";

        IntellectualProperty testIPRToUpdate = intellectualProperty(SECOND_DOMAIN_ID);
        tryToUpdateAndExceptResponseCode(
                testIPRToUpdate.getId(),
                new IntellectualPropertyData(newDescription),
                403);
    }

    @Test
    public void shouldUpdate() {
        IntellectualProperty testIPRToUpdate = intellectualProperty(DEFAULT_DOMAIN_ID, INTELLECTUAL_PROPERTY_DESCRIPTION);

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        String newDescription = "newDescription";

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + testIPRToUpdate.getId(),
                HttpMethod.PATCH,
                new HttpEntity<>(new IntellectualPropertyData(newDescription), headers),
                Void.class);

        assertEquals(200, response.getStatusCode().value());
        IntellectualProperty updated = intellectualPropertyRepository.getReferenceById(testIPRToUpdate.getId());
        assertEquals(newDescription, updated.getDescription());
    }

    private void tryToUpdateAndExceptResponseCode(int ipId, IntellectualPropertyData updateInfo, int responseCode) {
        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + ipId,
                HttpMethod.PATCH,
                new HttpEntity<>(updateInfo, headers),
                Void.class);

        assertEquals(responseCode, response.getStatusCode().value());
    }

    @Test
    public void shouldDeleteIntellectualProperty() {

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");

        IntellectualProperty intellectualProperty = intellectualProperty(DEFAULT_DOMAIN_ID);

        restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + intellectualProperty.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertFalse(intellectualPropertyRepository.existsById(intellectualProperty.getId()));
    }

    @Test
    public void shouldFailDeletionIntellectualPropertyContainsAnyDependantEntities() {

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");

        IntellectualProperty intellectualProperty = intellectualPropertyTaskTimeRecords(DEFAULT_DOMAIN_ID, "", LocalDate.now().minusDays(1), LocalDate.now());

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + intellectualProperty.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertEquals(400, response.getStatusCode().value());
        assertTrue(intellectualPropertyRepository.existsById(intellectualProperty.getId()));
    }

    @Test
    public void shouldFailDeletionIntellectualPropertyFromOtherDomain() {

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");

        IntellectualProperty intellectualProperty = intellectualProperty(SECOND_DOMAIN_ID, "");

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + intellectualProperty.getId(),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertEquals(403, response.getStatusCode().value());
        assertTrue(intellectualPropertyRepository.existsById(intellectualProperty.getId()));
    }

    @Test
    public void shouldFailDeletionIntellectualPropertyWhenEntityDoesntExist() {

        HttpHeaders headers = authenticatedHeaders(DEFAULT_DOMAIN_ID, "IPR");

        IntellectualProperty intellectualProperty = intellectualProperty(SECOND_DOMAIN_ID, "");

        ResponseEntity<Void> response = restTemplate.exchange(
                "http://localhost:" + serverPort + "/ipr/" + (intellectualProperty.getId() + 1),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                Void.class);

        assertEquals(404, response.getStatusCode().value());
        assertTrue(intellectualPropertyRepository.existsById(intellectualProperty.getId()));
    }
}