package pl.sg.ipr.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.ipr.api.IntellectualProperty;
import pl.sg.ipr.service.IntellectualPropertyService;

import java.util.List;
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

    @TokenBearerAuth(any = "IPR")
    public List<IntellectualProperty> getAll(int domainId) {
        return intellectualPropertyService.getAll(domainId)
                .stream()
                .map(intellectualProperty -> modelMapper.map(intellectualProperty, IntellectualProperty.class))
                .collect(Collectors.toList());
    }
}