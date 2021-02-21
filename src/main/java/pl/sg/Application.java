package pl.sg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.sg.accountant.model.accounts.Account;
import pl.sg.accountant.model.accounts.Client;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Category;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.transport.PiggyBankTO;
import pl.sg.accountant.transport.accounts.AccountTO;
import pl.sg.accountant.transport.accounts.ClientTO;
import pl.sg.accountant.transport.billings.BillingPeriodTO;
import pl.sg.accountant.transport.billings.CategoryTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@SpringBootApplication
public class Application {

    public static final String CREATE_ACCOUNT = "createAccount";
    public static final String UPDATE_ACCOUNT = "updateAccount";
    public static final String CREATE_CATEGORY = "createCategory";
    public static final String UPDATE_CATEGORY = "updateCategory";
    public static final String CREATE_CLIENT = "createClient";
    public static final String UPDATE_CLIENT = "updateClient";
    public static final String CREATE_PIGGY_BANK = "createPiggyBank";
    public static final String UPDATE_PIGGY_BANK = "updatePiggyBank";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Account.class, AccountTO.class);
        modelMapper.typeMap(BillingPeriod.class, BillingPeriodTO.class);
        modelMapper.typeMap(PiggyBank.class, PiggyBankTO.class);

        modelMapper.typeMap(AccountTO.class, Account.class, CREATE_ACCOUNT)
                .setConverter(context -> applyChanges(context.getSource(), new Account()));

        modelMapper.typeMap(AccountTO.class, Account.class, UPDATE_ACCOUNT)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(CategoryTO.class, Category.class, CREATE_CATEGORY)
                .setConverter(context -> applyChanges(context.getSource(), new Category()));

        modelMapper.typeMap(CategoryTO.class, Category.class, UPDATE_CATEGORY)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(ClientTO.class, Client.class, CREATE_CLIENT)
                .setConverter(context -> applyChanges(context.getSource(), new Client()));

        modelMapper.typeMap(ClientTO.class, Client.class, UPDATE_CLIENT)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(PiggyBankTO.class, PiggyBank.class, CREATE_PIGGY_BANK)
                .setConverter(context -> applyChanges(context.getSource(), new PiggyBank()));

        modelMapper.typeMap(PiggyBankTO.class, PiggyBank.class, UPDATE_PIGGY_BANK)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        return modelMapper;
    }

    private Account applyChanges(AccountTO source, Account destination) {
        destination.setName(source.getName());
        destination.setCurrency(source.getCurrency());
        return destination;
    }

    private Category applyChanges(CategoryTO source, Category destination) {
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        return destination;
    }

    private Client applyChanges(ClientTO source, Client destination) {
        destination.setName(source.getName());
        return destination;
    }

    private PiggyBank applyChanges(PiggyBankTO source, PiggyBank destination) {
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        destination.setBalance(source.getBalance());
        destination.setCurrency(source.getCurrency());
        destination.setMonthlyTopUp(source.getMonthlyTopUp());
        destination.setSavings(source.isSavings());
        return destination;
    }

    @Bean
    public Gson deserializer() {
        return new GsonBuilder().registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> {
            final String stringValue = json.getAsJsonPrimitive().getAsString();
            try {
                return LocalDate.parse(stringValue, DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException ex) {
                return LocalDate.parse(stringValue, DateTimeFormatter.ISO_DATE_TIME);
            }
        }).create();
    }
}
