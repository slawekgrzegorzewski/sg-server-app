package pl.sg.cubes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.application.model.Domain;
import pl.sg.cubes.model.CubeRecord;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CubeRecordRepository extends JpaRepository<CubeRecord, Integer> {

    public List<CubeRecord> findAllByDomain(Domain d);

    default List<CubeRecord> findAllByDomainAndDate(Domain d, LocalDate forDate) {
        return this.findAllByDomainAndRecordTimeBetween(d, forDate.atStartOfDay(), forDate.plusDays(1).atStartOfDay());
    }

    List<CubeRecord> findAllByDomainAndRecordTimeBetween(Domain d, LocalDateTime from, LocalDateTime to);

}
