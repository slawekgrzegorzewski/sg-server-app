package pl.sg.cubes.controller;

import pl.sg.application.model.Domain;
import pl.sg.cubes.model.CubeRecord;
import pl.sg.cubes.transport.CubeRecordTO;

import java.util.List;

public interface CubeRecordController {

    List<CubeRecordTO> all(Domain domain);

    CubeRecordTO create(CubeRecord data);

}
