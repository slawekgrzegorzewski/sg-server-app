package pl.sg.integrations.nodrigen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import pl.sg.integrations.nodrigen.model.NodrigenAccess;

import java.time.LocalDateTime;
import java.util.Optional;

public interface NodrigenAccessRepository extends JpaRepository<NodrigenAccess, Integer> {

    @Query("SELECT na FROM NodrigenAccess na WHERE na.archivedAt IS NULL")
    Optional<NodrigenAccess> getAccess();

    @Transactional
    @Modifying
    @Query("UPDATE NodrigenAccess na SET na.archivedAt = ?1 WHERE na.archivedAt IS NULL")
    void archiveAll(LocalDateTime archiveAtTime);

}