package pl.sg.cubes.service;

import pl.sg.application.model.Domain;
import pl.sg.cubes.model.CubeRecord;

import java.time.LocalDate;
import java.util.List;

public interface CubeRecordService {

    List<CubeRecord> getForDomain(Domain domain);

    List<CubeRecord> getForDomainAndDate(Domain domain, LocalDate forDate);

    CubeRecord record(CubeRecord record);
}
