package pl.sg.loans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sg.application.model.Domain;
import pl.sg.loans.model.RateStrategyConfig;

import java.util.List;
import java.util.UUID;

@Repository
public interface RateStrategyConfigRepository extends JpaRepository<RateStrategyConfig, Long> {
    List<RateStrategyConfig> findAllByDomain(Domain domain);

    RateStrategyConfig findByPublicId(UUID publicId);

}
