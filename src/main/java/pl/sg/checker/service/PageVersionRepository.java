package pl.sg.checker.service;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.checker.model.PageVersion;

import java.util.Optional;

public interface PageVersionRepository extends JpaRepository<PageVersion, Integer> {
    Optional<PageVersion> getTopByOrderByVersionTime();
}
