package pl.sg.application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.application.model.ReceivedRequest;

public interface ReceivedRequestRepository extends JpaRepository<ReceivedRequest, Integer> {
}