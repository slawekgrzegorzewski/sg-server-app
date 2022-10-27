package pl.sg.accountant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.sg.application.model.Domain;
import pl.sg.ipr.api.IntellectualPropertyCreateData;
import pl.sg.ipr.model.IntellectualProperty;
import pl.sg.ipr.repository.IntellectualPropertyRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {"spring.config.location=classpath:application-it.yml"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("dev")
@Testcontainers
public class IntellectualPropertyTest extends AbstractContainerBaseTest {

    public static final String INTELLECTUAL_PROPERTY_DESCRIPTION = "description";
    public static final int DOMAIN_ID = 1;
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    IntellectualPropertyRepository intellectualPropertyRepository;

    public IntellectualPropertyTest() {
    }

    @Test
    public void shouldFailGettingWhenUserHasNotIPRRole() throws Exception {
        clearApplicationUserRoles();
        runInTransaction(entityManager -> {
            IntellectualProperty intellectualProperty = new IntellectualProperty();
            intellectualProperty.setDomain(entityManager.getReference(Domain.class, 1));
            intellectualProperty.setStartDate(LocalDate.now().minusDays(1));
            intellectualProperty.setEndDate(LocalDate.now());
            intellectualProperty.setDescription("description");
            entityManager.persist(intellectualProperty);
        });
        Response response = get(1, "/ipr");
        assertEquals(403, response.code());
    }

    @Test
    public void shouldGetDomainOnlyData() throws Exception {
        setApplicationUserRoles("IPR");
        List<Integer> idsToRemove = callInTransaction(entityManager -> {
            List<Integer> ids = new ArrayList<>();
            IntellectualProperty intellectualProperty = new IntellectualProperty();
            intellectualProperty.setDomain(entityManager.getReference(Domain.class, 1));
            intellectualProperty.setStartDate(LocalDate.now().minusDays(1));
            intellectualProperty.setEndDate(LocalDate.now());
            intellectualProperty.setDescription("description");
            entityManager.persist(intellectualProperty);
            ids.add(intellectualProperty.getId());
            intellectualProperty = new IntellectualProperty();
            intellectualProperty.setDomain(entityManager.getReference(Domain.class, 2));
            intellectualProperty.setStartDate(LocalDate.now().minusDays(2));
            intellectualProperty.setEndDate(LocalDate.now().minusDays(1));
            intellectualProperty.setDescription("description2");
            entityManager.persist(intellectualProperty);
            ids.add(intellectualProperty.getId());
            return ids;
        });
        Response response = get(1, "/ipr");
        assertEquals(200, response.code());
        List<pl.sg.ipr.api.IntellectualProperty> intellectualProperties = objectMapper.readValue(response.body(), new TypeReference<>() {
        });
        deleteIntellectualProperty(idsToRemove.toArray(Integer[]::new));
    }

    @Test
    public void shouldFailCreationWhenUserHasNotIPRRole() throws Exception {
        clearApplicationUserRoles();
        Response response = put(1, "/ipr", put -> put.setEntity(new StringEntity(
                objectMapper.writeValueAsString(
                        new IntellectualPropertyCreateData(
                                LocalDate.now().minusDays(1),
                                LocalDate.now(),
                                "description")))));

        assertEquals(403, response.code());
    }

    @Test
    public void shouldCreateIntellectualProperty() throws Exception {
        setApplicationUserRoles("IPR");
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.minusDays(1);
        Response response = put(DOMAIN_ID, "/ipr",
                put -> put.setEntity(
                        new StringEntity(
                                objectMapper.writeValueAsString(
                                        new IntellectualPropertyCreateData(
                                                startDate,
                                                endDate,
                                                INTELLECTUAL_PROPERTY_DESCRIPTION)))));

        assertEquals(200, response.code());
        List<IntellectualProperty> all = intellectualPropertyRepository.findAll();
        assertEquals(1, all.size());
        assertEquals(DOMAIN_ID, all.get(0).getDomain().getId());
        assertEquals(startDate, all.get(0).getStartDate());
        assertEquals(endDate, all.get(0).getEndDate());
        assertEquals(INTELLECTUAL_PROPERTY_DESCRIPTION, all.get(0).getDescription());
        deleteIntellectualProperty(all.get(0).getId());
    }

    private void deleteIntellectualProperty(Integer... ids) {
        runInTransaction(entityManager -> {
            for (Integer id : ids) {
                entityManager.remove(entityManager.find(IntellectualProperty.class, id));
            }
        });
    }
}