package pl.sg.cubes.controller;

import org.modelmapper.ModelMapper;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.cubes.model.CubeRecord;
import pl.sg.cubes.service.CubeRecordService;
import pl.sg.cubes.transport.CubeRecordTO;

import java.util.List;
import java.util.stream.Collectors;

import static pl.sg.Application.CREATE_ACCOUNT;
import static pl.sg.Application.CREATE_CUBE_RECORD;

@RestController
@RequestMapping("/cube-record")
@Validated
public class CubeRecordRestController implements CubeRecordController {
    private final CubeRecordService cubeRecordService;
    private final ModelMapper mapper;

    public CubeRecordRestController(CubeRecordService cubeRecordService, ModelMapper mapper) {
        this.cubeRecordService = cubeRecordService;
        this.mapper = mapper;
    }

    @Override
    @GetMapping
    @TokenBearerAuth(any = {"CUBES"})
    public List<CubeRecordTO> all(@RequestDomain Domain domain) {
        List<CubeRecordTO> collect = this.cubeRecordService.getForDomain(domain).stream().map(cr -> mapper.map(cr, CubeRecordTO.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"CUBES"}, domainAdmin = true)
    public CubeRecordTO create(
            @RequestBodyWithDomain(
                    transportClass = CubeRecordTO.class,
                    mapperName = CREATE_CUBE_RECORD,
                    create = true,
                    domainAdmin = true)
                    CubeRecord record
    ) {
        return mapper.map(this.cubeRecordService.record(record), CubeRecordTO.class);
    }
}
