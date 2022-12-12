package pl.sg.kpir.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.kpir.KPiREntry;

public interface KPiRRepository extends JpaRepository<KPiREntry, Long> {
}
