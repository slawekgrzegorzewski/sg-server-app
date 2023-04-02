package pl.sg.ip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.ip.model.TimeRecordCategory;

import java.util.List;

public interface TimeRecordCategoryRepository extends JpaRepository<TimeRecordCategory, Integer> {
    List<TimeRecordCategory> getTimeRecordCategoriesByDomainId(int domainId);
}
