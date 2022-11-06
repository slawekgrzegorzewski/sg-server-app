package pl.sg.ip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.application.model.Domain;
import pl.sg.ip.model.TimeRecord;

import java.util.List;

public interface TimeRecordRepository extends JpaRepository<TimeRecord, Integer> {

    List<TimeRecord> findAllByDomain(Domain domain);

}
