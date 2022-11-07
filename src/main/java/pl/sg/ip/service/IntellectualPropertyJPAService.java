package pl.sg.ip.service;

import org.springframework.stereotype.Component;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ip.api.IntellectualPropertyData;
import pl.sg.ip.model.IPException;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.repository.IntellectualPropertyRepository;

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
    public IntellectualProperty create(int domainId, IntellectualPropertyData createData) {
        return intellectualPropertyRepository.save(
                new IntellectualProperty(
                        createData.startDate(),
                        createData.endDate(),
                        createData.description(),
                        this.domainRepository.getReferenceById(domainId)
                )
        );
    }

    @Override
    public void update(int domainId, int intellectualPropertyId, IntellectualPropertyData updateData) {
        IntellectualProperty toUpdate = intellectualPropertyRepository.findById(intellectualPropertyId).orElseThrow();
        IPValidator validator = new IPValidator(toUpdate);
        if (!validator.validateDomain(domainId)) {
            throw new IPException("Trying to update an IP for other domain.");
        }
        toUpdate.setDescription(updateData.description());
        if (!toUpdate.getStartDate().equals(updateData.startDate())) {
            if (!validator.validateStartDate(updateData.startDate())) {
                throw new IPException("Start date can not be set to after of any of tasks related to this IP.");
            }
            toUpdate.setStartDate(updateData.startDate());
        }
        if (!toUpdate.getEndDate().equals(updateData.endDate())) {
            if (!validator.validateEndDate(updateData.endDate())) {
                throw new IPException("End date can not be set to before of any of tasks related to this IP.");
            }
            toUpdate.setEndDate(updateData.endDate());
        }
        this.intellectualPropertyRepository.save(toUpdate);
    }
}
