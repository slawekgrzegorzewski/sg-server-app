package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.accountant.repository.ClientPaymentRepository;
import pl.sg.application.model.Domain;

import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClientPaymentsJPAService implements ClientPaymentsService {

    private final ClientPaymentRepository clientPaymentRepository;

    public ClientPaymentsJPAService(ClientPaymentRepository clientPaymentRepository) {
        this.clientPaymentRepository = clientPaymentRepository;
    }

    @Override
    public ClientPayment getOne(Integer paymentId) {
        return clientPaymentRepository.getOne(paymentId);
    }

    @Override
    public List<ClientPayment> clientPayments(Domain domain, YearMonth forMonth) {
        List<ClientPayment> clientPayments = clientPaymentRepository.forMonth(domain, forMonth);
        clientPaymentRepository.notFullyUsed(domain)
                .stream()
                .filter(cp -> clientPayments.stream().noneMatch(cp1 -> cp1.getId().equals(cp.getId())))
                .collect(Collectors.toCollection(() -> clientPayments));
        return clientPayments;
    }

    @Override
    public ClientPayment create(ClientPayment service) {
        service.setId(null);
        return this.clientPaymentRepository.save(service);
    }

    @Override
    public ClientPayment update(ClientPayment service) {
        return this.clientPaymentRepository.save(service);
    }
}
