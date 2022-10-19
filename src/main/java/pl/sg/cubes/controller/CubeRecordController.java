package pl.sg.cubes.controller;

import pl.sg.application.model.Domain;
import pl.sg.cubes.transport.CubeRecord;

import java.time.LocalDate;
import java.util.List;

public interface CubeRecordController {

    List<CubeRecord> all(Domain domain);

    List<CubeRecord> forDate(Domain domain, LocalDate date);

    CubeRecord create(pl.sg.cubes.model.CubeRecord data);

}
