package pl.sg.ipr.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.ipr.api.IntellectualProperty;
import pl.sg.ipr.api.IntellectualPropertyCreateData;
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

    @Override
    @GetMapping(produces = {"application/json", "plain/text"})
    @TokenBearerAuth(any = "IPR")
    public List<IntellectualProperty> getAll(@RequestHeader(value = "domainId") Integer domainId) {
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
            @RequestBody IntellectualPropertyCreateData createData) {
        return modelMapper.map(
                intellectualPropertyService.create(domainId, createData),
                IntellectualProperty.class
        );
    }
}
