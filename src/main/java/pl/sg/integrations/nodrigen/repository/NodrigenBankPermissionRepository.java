package pl.sg.integrations.nodrigen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.application.model.Domain;
import pl.sg.banks.model.BankAccount;
import pl.sg.integrations.nodrigen.model.NodrigenBankPermission;

import java.util.List;
import java.util.Optional;

public interface NodrigenBankPermissionRepository extends JpaRepository<NodrigenBankPermission, Integer> {

    @Query("SELECT nbp FROM NodrigenBankPermission nbp WHERE nbp.domain = ?1")
    List<NodrigenBankPermission> findAllPermissions(Domain domain);

    @Query("SELECT nbp FROM NodrigenBankPermission nbp WHERE nbp.domain = ?1 AND nbp.givenAt IS NULL and nbp.withdrawnAt IS NULL")
    List<NodrigenBankPermission> findPermissionsToConfirm(Domain domain);

    @Query("SELECT nbp FROM NodrigenBankPermission nbp WHERE nbp.domain = ?1 AND nbp.givenAt IS NOT NULL and nbp.withdrawnAt IS NULL")
    List<NodrigenBankPermission> findPermissionsGranted(Domain domain);

    @Query("SELECT nbp FROM NodrigenBankPermission nbp WHERE nbp.domain = ?1 " +
            "AND nbp.givenAt IS NOT NULL " +
            "AND nbp.withdrawnAt IS NULL " +
            "AND ?2 MEMBER OF nbp.bankAccounts")
    Optional<NodrigenBankPermission> getPermissionsGrantedForBankAccount(Domain domain, BankAccount bankAccount);

    @Query("SELECT nbp FROM NodrigenBankPermission nbp " +
            "WHERE nbp.domain = ?1 AND nbp.givenAt IS NULL and nbp.withdrawnAt IS NULL AND nbp.reference = ?2")
    Optional<NodrigenBankPermission> findPermissionToConfirm(Domain domain, String reference);
}
