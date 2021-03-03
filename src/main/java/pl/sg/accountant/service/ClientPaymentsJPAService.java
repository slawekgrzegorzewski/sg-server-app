package pl.sg.accountant.service;

import org.springframework.stereotype.Component;
import pl.sg.accountant.model.accounts.ClientPayment;
import pl.sg.accountant.repository.ClientPaymentRepository;
import pl.sg.application.model.Domain;

import java.util.List;

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
    public List<ClientPayment> clientPayments(Domain domain) {
        return clientPaymentRepository.findByDomain(domain);
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
