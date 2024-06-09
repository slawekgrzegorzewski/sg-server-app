package pl.sg.accountant.repository.bussines;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.bussines.BillOfSale;
import pl.sg.accountant.model.bussines.DebitInvoice;

import java.util.UUID;

public interface DebitInvoiceRepository extends JpaRepository<DebitInvoice, Integer> {
    DebitInvoice findBillOfSaleByPublicId(UUID publicId);
}
