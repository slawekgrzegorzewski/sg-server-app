package pl.sg.application.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserLoginRepository extends JpaRepository<ApplicationUserLogin, Integer> {
}
