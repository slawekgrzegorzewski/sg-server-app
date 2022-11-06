package pl.sg.ip.service;

import org.springframework.stereotype.Component;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ip.api.IntellectualPropertyData;
import pl.sg.ip.model.IPException;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;
import pl.sg.ip.model.TimeRecord;
import pl.sg.ip.repository.IntellectualPropertyRepository;

import java.time.LocalDate;
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
        validateDomain(domainId, toUpdate);
        toUpdate.setDescription(updateData.description());
        if (!toUpdate.getStartDate().equals(updateData.startDate())) {
            validateStartDate(updateData.startDate(), toUpdate);
            toUpdate.setStartDate(updateData.startDate());
        }
        if (!toUpdate.getEndDate().equals(updateData.endDate())) {
            validateEndDate(updateData.endDate(), toUpdate);
            toUpdate.setEndDate(updateData.endDate());
        }
        this.intellectualPropertyRepository.save(toUpdate);
    }

    private void validateDomain(int domainId, IntellectualProperty updatingIntellectualProperty) {
        if (domainId != updatingIntellectualProperty.getDomain().getId()) {
            throw new IPException("Trying to update an IP for other domain.");
        }
    }

    private void validateStartDate(LocalDate startDate, IntellectualProperty intellectualProperty) {
        boolean isCorrect = intellectualProperty.tasks().stream()
                .map(Task::getTimeRecords)
                .flatMap(List::stream)
                .map(TimeRecord::getDate)
                .min(LocalDate::compareTo)
                .map(min -> !min.isBefore(startDate))
                .orElse(true);
        if (!isCorrect) {
            throw new IPException("Start date can not be set to after of any of tasks related to this IP.");
        }
    }

    private void validateEndDate(LocalDate endDate, IntellectualProperty intellectualProperty) {
        boolean isCorrect = intellectualProperty.tasks().stream()
                .map(Task::getTimeRecords)
                .flatMap(List::stream)
                .map(TimeRecord::getDate)
                .max(LocalDate::compareTo)
                .map(max -> !max.isAfter(endDate))
                .orElse(true);
        if (!isCorrect) {
            throw new IPException("End date can not be set to before of any of tasks related to this IP.");
        }
    }
}
