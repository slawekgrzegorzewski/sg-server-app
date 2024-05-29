package pl.sg.loans.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.sg.application.model.Domain;
import pl.sg.loans.model.RepaymentDayStrategyConfig;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepaymentDayStrategyConfigRepository extends JpaRepository<RepaymentDayStrategyConfig, Long> {
    List<RepaymentDayStrategyConfig> findAllByDomain(Domain domain);

    RepaymentDayStrategyConfig findByPublicId(UUID publicId);
}
