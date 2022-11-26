package pl.sg.ip.service;

import org.springframework.stereotype.Component;
import pl.sg.application.ForbiddenException;
import pl.sg.application.repository.DomainRepository;
import pl.sg.ip.api.IntellectualPropertyData;
import pl.sg.ip.api.TaskData;
import pl.sg.ip.model.IPException;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;
import pl.sg.ip.repository.IntellectualPropertyRepository;
import pl.sg.ip.repository.TaskRepository;
import pl.sg.ip.service.validator.Validator;
import pl.sg.ip.service.validator.ValidatorFactory;

import java.util.Collection;
import java.util.List;

@Component
public class IntellectualPropertyJPAService implements IntellectualPropertyService {

    private final DomainRepository domainRepository;
    private final IntellectualPropertyRepository intellectualPropertyRepository;
    private final TaskRepository taskRepository;
    private final ValidatorFactory validatorFactory;

    public IntellectualPropertyJPAService(DomainRepository domainRepository, IntellectualPropertyRepository intellectualPropertyRepository, TaskRepository taskRepository, ValidatorFactory validatorFactory) {
        this.domainRepository = domainRepository;
        this.intellectualPropertyRepository = intellectualPropertyRepository;
        this.taskRepository = taskRepository;
        this.validatorFactory = validatorFactory;
    }

    @Override
    public Collection<IntellectualProperty> getAll(int domainId) {
        return intellectualPropertyRepository.findAllByDomain_Id(domainId);
    }

    @Override
    public IntellectualProperty create(int domainId, IntellectualPropertyData createData) {
        return intellectualPropertyRepository.save(new IntellectualProperty(createData.description(), this.domainRepository.getReferenceById(domainId)));
    }

    @Override
    public void update(int domainId, int intellectualPropertyId, IntellectualPropertyData updateData) {
        IntellectualProperty toUpdate = intellectualPropertyRepository.findById(intellectualPropertyId).orElseThrow();
        Validator validator = validatorFactory.validator(toUpdate);
        if (!validator.validateDomain(domainId)) {
            throw new ForbiddenException("Trying to update an IP for other domain.");
        }
        toUpdate.setDescription(updateData.description());
        this.intellectualPropertyRepository.save(toUpdate);
    }

    @Override
    public void delete(int domainId, int intellectualPropertyId) {
        IntellectualProperty toDelete = intellectualPropertyRepository.findById(intellectualPropertyId).orElseThrow();
        Validator validator = validatorFactory.validator(toDelete);
        if (!validator.validateDomain(domainId)) {
            throw new ForbiddenException("Trying to update an IP for other domain.");
        }
        if (!validator.validateDeletion()) {
            throw new IPException("This entity refers task and thus can not be deleted");
        }
        this.intellectualPropertyRepository.delete(toDelete);
    }

    @Override
    public Collection<Task> getTasksOfIntellectualProperty(int domainId, int intellectualPropertyId) {
        IntellectualProperty ip = intellectualPropertyRepository.findById(intellectualPropertyId).orElseThrow();
        if (!validatorFactory.validator(ip).validateDomain(domainId)) {
            throw new ForbiddenException("Trying to get tasks from IP from other domain.");
        }
        return taskRepository.findAllByIntellectualProperty(ip);
    }

    @Override
    public void createTask(int domainId, int intellectualPropertyId, TaskData taskData) {
        IntellectualProperty addTaskTo = intellectualPropertyRepository.findById(intellectualPropertyId).orElseThrow();
        Validator validator = validatorFactory.validator(addTaskTo);
        if (!validator.validateDomain(domainId)) {
            throw new ForbiddenException("Trying to add task to an IP from other domain.");
        }
        this.taskRepository.save(new Task(taskData.description(), taskData.coAuthors(), List.of(), addTaskTo, List.of()));
        this.intellectualPropertyRepository.save(addTaskTo);
    }
}
