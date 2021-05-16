package pl.sg.cubes.service;

import pl.sg.accountant.model.AccountantSettings;
import pl.sg.application.model.Domain;
import pl.sg.cubes.model.CubeRecord;
import pl.sg.cubes.model.CubesType;

import java.time.Duration;
import java.util.List;

public interface CubeRecordService {

    List<CubeRecord> getForDomain(Domain domain);

    CubeRecord record(CubeRecord record);
}
