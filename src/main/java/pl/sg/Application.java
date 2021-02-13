package pl.sg;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.transport.PiggyBankTO;
import pl.sg.accountant.transport.accounts.AccountTO;
import pl.sg.accountant.transport.billings.BillingPeriodTO;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        modelMapper
                .typeMap(Account.class, AccountTO.class);
        modelMapper
                .typeMap(BillingPeriod.class, BillingPeriodTO.class)
                .addMapping(BillingPeriod::getId, BillingPeriodTO::setUserId);
        modelMapper
                .typeMap(PiggyBank.class, PiggyBankTO.class)
                .addMapping(PiggyBank::getId, PiggyBankTO::setUserId);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
