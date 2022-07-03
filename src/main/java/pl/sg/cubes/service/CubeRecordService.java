package pl.sg.cubes.service;

import pl.sg.application.model.Domain;
import pl.sg.cubes.model.CubeRecord;
import pl.sg.cubes.model.CubeStatistics;
import pl.sg.cubes.model.CubesType;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CubeRecordService {

    List<CubeRecord> getForDomain(Domain domain);

    List<CubeRecord> getForDomainAndDate(Domain domain, LocalDate forDate);

    CubeRecord record(CubeRecord record);

    Map<CubesType, CubeStatistics> getStatistics(Domain domain);
}
