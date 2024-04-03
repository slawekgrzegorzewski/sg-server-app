package pl.sg.pjm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sg.pjm.entities.TranslatedVerse;

@Repository
public interface TranslatedVersesRepository extends JpaRepository<TranslatedVerse, Long> {
}
