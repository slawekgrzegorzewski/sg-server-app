package pl.sg.banks.integrations.nodrigen.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sg.application.repository.DomainRepository;
import pl.sg.banks.services.BankAccountService;

@Component
public class NodrigenDataFetcher {

    private final DomainRepository domainRepository;
    private final BankAccountService bankAccountService;

    public NodrigenDataFetcher(DomainRepository domainRepository, BankAccountService bankAccountService) {
        this.domainRepository = domainRepository;
        this.bankAccountService = bankAccountService;
    }

    @Scheduled(cron = "0 0 0,6,12,18 * * *", zone = "Europe/Warsaw")
    public void fetchAllTransactions() {
        domainRepository.findAll().forEach(bankAccountService::fetchAllTransactions);
    }

    @Scheduled(cron = "0 5 0,6,12,18 * * *", zone = "Europe/Warsaw")
    public void fetchAllBalances() {
        domainRepository.findAll().forEach(bankAccountService::fetchAllBalances);
    }
}
