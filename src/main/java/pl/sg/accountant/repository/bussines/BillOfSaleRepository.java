package pl.sg.accountant.repository.bussines;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.bussines.BillOfPurchase;
import pl.sg.accountant.model.bussines.BillOfSale;

import java.util.UUID;

public interface BillOfSaleRepository extends JpaRepository<BillOfSale, Integer> {
    BillOfSale findBillOfSaleByPublicId(UUID publicId);
}
