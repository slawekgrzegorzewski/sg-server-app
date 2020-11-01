package pl.sg;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.billings.BillingPeriod;
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
                .typeMap(Account.class, AccountTO.class)
                .addMapping(a -> a.getApplicationUser().getLogin(), AccountTO::setUserName);
        modelMapper
                .typeMap(BillingPeriod.class, BillingPeriodTO.class)
                .addMapping(b -> b.getApplicationUser().getLogin(), BillingPeriodTO::setUserName);
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
