package pl.sg.ipr.service;

import org.springframework.stereotype.Component;
import pl.sg.application.model.Domain;
import pl.sg.ipr.model.IntellectualProperty;
import pl.sg.ipr.repository.IntellectualPropertyRepository;
import pl.sg.ipr.transport.IntellectualPropertyCreateData;

@Component
public class IntellectualPropertyJPAService implements IntellectualPropertyService {

    private final IntellectualPropertyRepository intellectualPropertyRepository;

    public IntellectualPropertyJPAService(IntellectualPropertyRepository intellectualPropertyRepository) {
        this.intellectualPropertyRepository = intellectualPropertyRepository;
    }

    @Override
    public IntellectualProperty create(IntellectualPropertyCreateData createData, Domain domain) {
        return intellectualPropertyRepository.save(
                new IntellectualProperty(
                        createData.startDate(),
                        createData.endDate(),
                        createData.description(),
                        domain
                )
        );
    }
}
