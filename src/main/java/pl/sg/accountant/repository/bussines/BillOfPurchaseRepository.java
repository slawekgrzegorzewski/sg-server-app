package pl.sg.accountant.repository.bussines;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.bussines.BillOfPurchase;
import pl.sg.accountant.model.bussines.Supplier;
import pl.sg.application.model.Domain;

import java.util.List;
import java.util.UUID;

public interface BillOfPurchaseRepository extends JpaRepository<BillOfPurchase, Integer> {
    BillOfPurchase findBillOfPurchaseByPublicId(UUID publicId);
}
