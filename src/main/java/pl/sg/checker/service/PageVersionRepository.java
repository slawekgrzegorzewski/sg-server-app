package pl.sg.checker.service;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.checker.model.CheckerTask;
import pl.sg.checker.model.PageVersion;

import java.util.List;

public interface PageVersionRepository extends JpaRepository<PageVersion, Integer> {
    List<PageVersion> getByTask(CheckerTask task);
}
