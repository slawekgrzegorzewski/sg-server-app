package pl.sg.cubes.service;

import org.springframework.stereotype.Component;
import pl.sg.application.model.Domain;
import pl.sg.cubes.model.CubeRecord;
import pl.sg.cubes.model.CubesType;
import pl.sg.cubes.repository.CubeRecordRepository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Component
public class CubeRecordServiceJPAService implements CubeRecordService {
    private final CubeRecordRepository cubeRecordRepository;

    public CubeRecordServiceJPAService(CubeRecordRepository cubeRecordRepository) {
        this.cubeRecordRepository = cubeRecordRepository;
    }

    @Override
    public List<CubeRecord> getForDomain(Domain domain) {
        return cubeRecordRepository.findAllByDomain(domain);
    }

    @Override
    public CubeRecord record(CubeRecord record) {
        return this.cubeRecordRepository.save(record);
    }
}
