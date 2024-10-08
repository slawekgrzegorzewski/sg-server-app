package pl.sg.banks.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.sg.application.repository.DomainRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Component
public class BankAccountsDataFetcher {

    private final DomainRepository domainRepository;
    private final BankAccountService bankAccountService;

    public BankAccountsDataFetcher(DomainRepository domainRepository, BankAccountService bankAccountService) {
        this.domainRepository = domainRepository;
        this.bankAccountService = bankAccountService;
    }

    @Scheduled(cron = "${cron.fetch-transactions}", zone = "Europe/Warsaw")
    public void fetchAllTransactions() {
        domainRepository.findAll().forEach(bankAccountService::fetchAllTransactions);
    }

    @Scheduled(cron = "${cron.fetch-accounts}", zone = "Europe/Warsaw")
    public void fetchAllBalances() {
        domainRepository.findAll().forEach(bankAccountService::fetchAllBalances);
    }

    @Scheduled(cron = "${cron.fetch-accounts-monthly}", zone = "Europe/Warsaw")
    public void fetchAllBalancesAtTheEndOfMonth() {
        if (LocalDate.now().equals(YearMonth.now().atEndOfMonth())) {
            domainRepository.findAll().forEach(bankAccountService::fetchAllBalances);
        }
    }
}