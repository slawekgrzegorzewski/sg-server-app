package pl.sg.accountant.repository.bussines;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sg.accountant.model.bussines.BillOfSale;
import pl.sg.accountant.model.bussines.CreditInvoice;

import java.util.UUID;

public interface CreditInvoiceRepository extends JpaRepository<CreditInvoice, Integer> {
    CreditInvoice findBillOfSaleByPublicId(UUID publicId);
}
