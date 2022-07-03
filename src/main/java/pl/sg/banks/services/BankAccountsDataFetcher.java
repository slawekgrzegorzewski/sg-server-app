package pl.sg.banks.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sg.application.repository.DomainRepository;

@Component
public class BankAccountsDataFetcher {

    private final DomainRepository domainRepository;
    private final BankAccountService bankAccountService;

    public BankAccountsDataFetcher(DomainRepository domainRepository, BankAccountService bankAccountService) {
        this.domainRepository = domainRepository;
        this.bankAccountService = bankAccountService;
    }

    @Scheduled(cron = "0 0 8,11,14,18 * * *", zone = "Europe/Warsaw")
    public void fetchAllTransactions() {
        domainRepository.findAll().forEach(bankAccountService::fetchAllTransactions);
    }

    @Scheduled(cron = "0 5 8,11,14,18 * * *", zone = "Europe/Warsaw")
    public void fetchAllBalances() {
        domainRepository.findAll().forEach(bankAccountService::fetchAllBalances);
    }
}
