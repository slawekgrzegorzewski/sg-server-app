package pl.sg.syr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.syr.model.SYR;

import java.time.Year;
import java.util.Optional;

public interface SYRRepository extends JpaRepository<SYR, Integer> {
    Optional<SYR> findFirstByYearEquals(Year year);
}
