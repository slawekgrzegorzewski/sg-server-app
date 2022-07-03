package pl.sg.banks.integrations.nodrigen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.application.model.Domain;
import pl.sg.banks.integrations.nodrigen.model.NodrigenBankPermission;

import java.util.List;
import java.util.Optional;

public interface NodrigenBankPermissionRepository extends JpaRepository<NodrigenBankPermission, Integer> {
    @Query("SELECT nbp FROM NodrigenBankPermission nbp WHERE nbp.domain = ?1 AND nbp.givenAt IS NULL and nbp.withdrawnAt IS NULL")
    List<NodrigenBankPermission> findPermissionsToConfirm(Domain d);

    @Query("SELECT nbp FROM NodrigenBankPermission nbp WHERE nbp.domain = ?1 AND nbp.givenAt IS NOT NULL and nbp.withdrawnAt IS NULL")
    List<NodrigenBankPermission> findPermissionsGranted(Domain d);

    @Query("SELECT nbp FROM NodrigenBankPermission nbp " +
            "WHERE nbp.domain = ?1 AND nbp.givenAt IS NULL and nbp.withdrawnAt IS NULL AND nbp.reference = ?2")
    Optional<NodrigenBankPermission> findPermissionToConfirm(Domain domain, String reference);
}
