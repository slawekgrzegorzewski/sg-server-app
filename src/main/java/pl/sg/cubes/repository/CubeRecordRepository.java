package pl.sg.cubes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.application.model.Domain;
import pl.sg.cubes.model.CubeRecord;

import java.util.List;

public interface CubeRecordRepository extends JpaRepository<CubeRecord, Integer> {

    public List<CubeRecord> findAllByDomain(Domain d);
}
