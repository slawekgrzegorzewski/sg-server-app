package pl.sg.ipr.service;

import org.springframework.stereotype.Component;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ipr.model.IntellectualProperty;
import pl.sg.ipr.repository.IntellectualPropertyRepository;
import pl.sg.ipr.api.IntellectualPropertyCreateData;

import java.util.List;

@Component
public class IntellectualPropertyJPAService implements IntellectualPropertyService {

    private final DomainRepository domainRepository;
    private final IntellectualPropertyRepository intellectualPropertyRepository;

    public IntellectualPropertyJPAService(DomainRepository domainRepository, IntellectualPropertyRepository intellectualPropertyRepository) {
        this.domainRepository = domainRepository;
        this.intellectualPropertyRepository = intellectualPropertyRepository;
    }

    @Override
    public List<IntellectualProperty> getAll(int domainId) {
        return intellectualPropertyRepository.findAllByDomain_Id(domainId);
    }

    @Override
    public IntellectualProperty create(int domainId, IntellectualPropertyCreateData createData) {
        return intellectualPropertyRepository.save(
                new IntellectualProperty(
                        createData.startDate(),
                        createData.endDate(),
                        createData.description(),
                        this.domainRepository.getReferenceById(domainId)
                )
        );
    }
}
