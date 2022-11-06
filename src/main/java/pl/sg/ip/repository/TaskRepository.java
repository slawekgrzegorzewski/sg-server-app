package pl.sg.ip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.application.model.Domain;
import pl.sg.ip.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t JOIN t.intellectualProperty ip WHERE ip.domain = ?1")
    List<Task> findAllByDomain(Domain domain);

}
