package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.AccountsException;
import pl.sg.accountant.model.ledger.ClientPayment;
import pl.sg.accountant.model.ledger.PerformedService;
import pl.sg.accountant.model.ledger.PerformedServicePayment;
import pl.sg.accountant.repository.PerformedServicePaymentRepository;
import pl.sg.application.model.ApplicationUser;
import pl.sg.application.model.Domain;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Component
public class PerformedServicePaymentsJPAService implements PerformedServicePaymentsService {

    private final PerformedServicePaymentRepository performedServicePaymentRepository;
    private final ClientPaymentsService clientPaymentsService;
    private final PerformedServicesService performedServicesService;

    public PerformedServicePaymentsJPAService(PerformedServicePaymentRepository performedServicePaymentRepository,
                                              ClientPaymentsService clientPaymentsService,
                                              PerformedServicesService performedServicesService) {
        this.performedServicePaymentRepository = performedServicePaymentRepository;
        this.clientPaymentsService = clientPaymentsService;
        this.performedServicesService = performedServicesService;
    }

    @Override
    public List<PerformedServicePayment> payments(Domain domain) {
        return performedServicePaymentRepository.findByDomain(domain);
    }

    @Override
    public PerformedServicePayment create(ApplicationUser user, PerformedServicePayment payment) {
        payment.setId(null);
        ClientPayment clientPayment = clientPaymentsService.getOne(payment.getClientPayment().getId());
        PerformedService performedService = performedServicesService.getOne(payment.getPerformedService().getId());
        user.validateDomain(clientPayment.getDomain());
        user.validateDomain(performedService.getDomain());
        validateSameCurrency(clientPayment.getCurrency(), performedService.getCurrency());
        validatePaymentCoverage(clientPayment, payment);
        validateNoOverpayment(performedService, payment);

        List<PerformedServicePayment> otherPSPs = this.performedServicePaymentRepository
                .findByClientPaymentAndPerformedService(clientPayment, performedService);
        payment.setPrice(otherPSPs.stream()
                .map(PerformedServicePayment::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .add(payment.getPrice()));
        otherPSPs.forEach(performedServicePaymentRepository::delete);
        return this.performedServicePaymentRepository.save(payment);
    }

    private void validateSameCurrency(Currency first, Currency second) {
        if (!first.getCurrencyCode().equals(second.getCurrencyCode())) {
            throw new AccountsException("Payment has to be for the same currency.");
        }
    }

    private void validateNoOverpayment(PerformedService performedService, PerformedServicePayment payment) {
        BigDecimal sum = performedService.getPayments()
                .stream()
                .map(PerformedServicePayment::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .add(payment.getPrice());
        if (sum.compareTo(performedService.getPrice()) > 0) {
            throw new AccountsException("After adding this payment a service will be overpaid.");
        }
    }

    private void validatePaymentCoverage(ClientPayment clientPayment, PerformedServicePayment payment) {
        BigDecimal sum = clientPayment.getServices()
                .stream()
                .map(PerformedServicePayment::getPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .add(payment.getPrice());
        if (sum.compareTo(clientPayment.getPrice()) > 0) {
            throw new AccountsException("After adding this payment a there will be not enough money in client's payment.");
        }
    }

    @Override
    public PerformedServicePayment update(PerformedServicePayment payment) {
        return this.performedServicePaymentRepository.save(payment);
    }
}
