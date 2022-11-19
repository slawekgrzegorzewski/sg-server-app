package pl.sg.ip.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {

    List<Task> findAllByIntellectualProperty(IntellectualProperty intellectualProperty);

}
