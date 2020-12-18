package pl.sg.checker.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.checker.model.CheckerTask;
import pl.sg.checker.model.CheckerTaskHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckerTaskHistoryRepository extends JpaRepository<CheckerTaskHistory, Integer> {
}
