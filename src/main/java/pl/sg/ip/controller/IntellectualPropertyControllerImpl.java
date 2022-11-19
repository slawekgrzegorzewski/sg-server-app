package pl.sg.ip.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.ip.api.IntellectualProperty;
import pl.sg.ip.api.IntellectualPropertyData;
import pl.sg.ip.api.Task;
import pl.sg.ip.api.TaskData;
import pl.sg.ip.service.IntellectualPropertyService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/ipr")
public class IntellectualPropertyControllerImpl implements IntellectualPropertyController {

    private final IntellectualPropertyService intellectualPropertyService;
    private final ModelMapper modelMapper;

    public IntellectualPropertyControllerImpl(IntellectualPropertyService intellectualPropertyService, ModelMapper modelMapper) {
        this.intellectualPropertyService = intellectualPropertyService;
        this.modelMapper = modelMapper;
    }

    @Override
    @GetMapping(produces = {"application/json", "plain/text"})
    @TokenBearerAuth(any = "IPR")
    public Collection<IntellectualProperty> getAll(@RequestHeader(value = "domainId") Integer domainId) {
        return intellectualPropertyService.getAll(domainId)
                .stream()
                .map(intellectualProperty -> modelMapper.map(intellectualProperty, IntellectualProperty.class))
                .collect(Collectors.toList());
    }

    @Override
    @PutMapping(produces = {"application/json", "plain/text"})
    @TokenBearerAuth(any = "IPR")
    public IntellectualProperty create(
            @RequestHeader(value = "domainId") Integer domainId,
            @RequestBody IntellectualPropertyData createData) {
        return modelMapper.map(
                intellectualPropertyService.create(domainId, createData),
                IntellectualProperty.class
        );
    }

    @Override
    @PatchMapping(value = "/{id}", produces = {"application/json", "plain/text"})
    @TokenBearerAuth(any = "IPR")
    public void update(
            @RequestHeader(value = "domainId") int domainId,
            @PathVariable("id") int intellectualPropertyId,
            @RequestBody IntellectualPropertyData createData) {
        intellectualPropertyService.update(domainId, intellectualPropertyId, createData);
    }

    @Override
    @DeleteMapping(value = "/{id}", produces = {"plain/text"})
    @TokenBearerAuth(any = "IPR")
    public void delete(
            @RequestHeader(value = "domainId") int domainId,
            @PathVariable("id") int intellectualPropertyId) {
        intellectualPropertyService.delete(domainId, intellectualPropertyId);
    }

    @Override
    @GetMapping(path = "{id}/task", produces = {"application/json", "plain/text"})
    @TokenBearerAuth(any = "IPR")
    public Collection<Task> getAllTasks(
            @RequestHeader(value = "domainId") int domainId,
            @PathVariable("id") int intellectualPropertyId) {
        return intellectualPropertyService.getTasksOfIntellectualProperty(domainId, intellectualPropertyId).stream()
                .map(task -> modelMapper.map(task, Task.class))
                .collect(Collectors.toList());
    }

    @Override
    @PostMapping(path = "{id}/task", produces = {"plain/text"})
    @TokenBearerAuth(any = "IPR")
    public void createTask(
            @RequestHeader(value = "domainId") int domainId,
            @PathVariable("id") int intellectualPropertyId,
            @RequestBody TaskData taskData) {
        intellectualPropertyService.createTask(domainId, intellectualPropertyId, taskData);
    }
}
