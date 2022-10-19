package pl.sg.cubes.controller;

import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.sg.application.model.Domain;
import pl.sg.application.security.annotations.RequestBodyWithDomain;
import pl.sg.application.security.annotations.RequestDomain;
import pl.sg.application.security.annotations.TokenBearerAuth;
import pl.sg.cubes.service.CubeRecordService;
import pl.sg.cubes.transport.CubeRecord;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<CubeRecord> all(@RequestDomain Domain domain) {
        return this.cubeRecordService.getForDomain(domain).stream().map(cr -> mapper.map(cr, CubeRecord.class)).collect(Collectors.toList());
    }

    @Override
    @GetMapping("/{forDate}")
    @TokenBearerAuth(any = {"CUBES"})
    public List<CubeRecord> forDate(@RequestDomain Domain domain,
                                    @Nullable @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate forDate) {
        if (forDate == null) {
            forDate = LocalDate.now();
        }
        return this.cubeRecordService.getForDomainAndDate(domain, forDate).stream().map(cr -> mapper.map(cr, CubeRecord.class)).collect(Collectors.toList());
    }

    @Override
    @PutMapping
    @TokenBearerAuth(any = {"CUBES"}, domainAdmin = true)
    public CubeRecord create(
            @RequestBodyWithDomain(
                    transportClass = CubeRecord.class,
                    mapperName = CREATE_CUBE_RECORD,
                    create = true,
                    domainAdmin = true)
            pl.sg.cubes.model.CubeRecord record
    ) {
        return mapper.map(this.cubeRecordService.record(record), CubeRecord.class);
    }
}
