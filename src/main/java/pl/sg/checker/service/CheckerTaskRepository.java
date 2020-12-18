package pl.sg.checker.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.checker.model.CheckerTask;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckerTaskRepository extends JpaRepository<CheckerTask, Integer> {
    @Query("SELECT ct FROM CheckerTask ct JOIN ct.forUser au JOIN au.userLogins aul WHERE aul.login = ?1")
    List<CheckerTask> findAllByApplicationUserLogin(String userName);

    @Query("SELECT ct FROM CheckerTask ct WHERE ct.nextRun IS NULL OR ct.nextRun < ?1")
    List<CheckerTask> findTasksToRunAtTime(LocalDateTime time);

    default List<CheckerTask> findTasksToRun() {
        return this.findTasksToRunAtTime(LocalDateTime.now());
    }
}
