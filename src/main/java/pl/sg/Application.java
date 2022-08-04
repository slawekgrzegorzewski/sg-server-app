package pl.sg;

import com.google.gson.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.SourceGetter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.sg.accountant.model.HolidayCurrencies;
import pl.sg.accountant.model.accounts.*;
import pl.sg.accountant.model.billings.BillingPeriod;
import pl.sg.accountant.model.billings.Category;
import pl.sg.accountant.model.billings.PiggyBank;
import pl.sg.accountant.transport.FinancialTransactionTO;
import pl.sg.accountant.transport.HolidayCurrenciesTO;
import pl.sg.accountant.transport.PiggyBankTO;
import pl.sg.accountant.transport.accounts.*;
import pl.sg.accountant.transport.billings.BillingPeriodTO;
import pl.sg.accountant.transport.billings.CategoryTO;
import pl.sg.application.transport.DomainTO;
import pl.sg.banks.model.BankAccount;
import pl.sg.banks.transport.BankAccountTO;
import pl.sg.cubes.model.CubeRecord;
import pl.sg.cubes.transport.CubeRecordTO;
import pl.sg.integrations.nodrigen.model.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.model.NodrigenTransactionsToImport;
import pl.sg.integrations.nodrigen.transport.NodrigenBankPermissionTO;
import pl.sg.integrations.nodrigen.transport.NodrigenTransactionsToImportTO;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.function.Function;

@SpringBootApplication
public class Application {

    public static final String CREATE_ACCOUNT = "createAccount";
    public static final String UPDATE_ACCOUNT = "updateAccount";
    public static final String CREATE_CATEGORY = "createCategory";
    public static final String UPDATE_CATEGORY = "updateCategory";
    public static final String CREATE_CLIENT = "createClient";
    public static final String UPDATE_CLIENT = "updateClient";
    public static final String CREATE_CLIENT_PAYMENT = "createClientPayment";
    public static final String UPDATE_CLIENT_PAYMENT = "updateClientPayment";
    public static final String CREATE_PERFORMED_SERVICE = "createPerformedService";
    public static final String UPDATE_PERFORMED_SERVICE = "updatePerformedService";
    public static final String CREATE_PERFORMED_SERVICE_PAYMENT = "createPerformedServicePayment";
    public static final String UPDATE_PERFORMED_SERVICE_PAYMENT = "updatePerformedServicePayment";
    public static final String CREATE_PIGGY_BANK = "createPiggyBank";
    public static final String UPDATE_PIGGY_BANK = "updatePiggyBank";
    public static final String CREATE_SERVICE = "createService";
    public static final String UPDATE_SERVICE = "updateService";
    public static final String CREATE_CUBE_RECORD = "createCubeRecord";
    public static final String UPDATE_CUBE_RECORD = "updateCubeRecord";
    public static final String CREATE_HOLIDAY_CURRENCIES = "createHolidayCurrencies";
    public static final String UPDATE_HOLIDAY_CURRENCIES = "updateHolidayCurrencies";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        final ModelMapper modelMapper = new ModelMapper();
        modelMapper.typeMap(Account.class, AccountTO.class);
        modelMapper.typeMap(BillingPeriod.class, BillingPeriodTO.class);
        modelMapper.typeMap(PiggyBank.class, PiggyBankTO.class);
        modelMapper.typeMap(ClientPayment.class, ClientPaymentTO.class)
                .addMapping(ClientPayment::getServices, ClientPaymentTO::setServiceRelations);
        modelMapper.typeMap(PerformedService.class, PerformedServiceTO.class)
                .addMapping(PerformedService::getPayments, PerformedServiceTO::setClientPaymentsRelations);

        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::getDate, null), PerformedServicePaymentTO::setDate);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::getCurrency, null), PerformedServicePaymentTO::setCurrency);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::isInvoice, false), PerformedServicePaymentTO::setInvoice);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::isBillOfSale, false), PerformedServicePaymentTO::setBillOfSale);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::isBillOfSaleAsInvoice, false), PerformedServicePaymentTO::setBillOfSaleAsInvoice);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::isNotRegistered, false), PerformedServicePaymentTO::setNotRegistered);

        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::getDate, null), PerformedServicePaymentSimpleTO::setDate);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::getCurrency, null), PerformedServicePaymentSimpleTO::setCurrency);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::isInvoice, false), PerformedServicePaymentSimpleTO::setInvoice);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::isBillOfSale, false), PerformedServicePaymentSimpleTO::setBillOfSale);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::isBillOfSaleAsInvoice, false), PerformedServicePaymentSimpleTO::setBillOfSaleAsInvoice);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(ClientPayment::isNotRegistered, false), PerformedServicePaymentSimpleTO::setNotRegistered);

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

        modelMapper.typeMap(ClientPaymentTO.class, ClientPayment.class, CREATE_CLIENT_PAYMENT)
                .setConverter(context -> applyChanges(context.getSource(), modelMapper));

        modelMapper.typeMap(ClientPaymentTO.class, ClientPayment.class, UPDATE_CLIENT_PAYMENT)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(PerformedServiceTO.class, PerformedService.class, CREATE_PERFORMED_SERVICE)
                .setConverter(context -> applyChanges(context.getSource(), new PerformedService(), modelMapper));

        modelMapper.typeMap(PerformedServiceTO.class, PerformedService.class, UPDATE_PERFORMED_SERVICE)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(PerformedServicePaymentTO.class, PerformedServicePayment.class, CREATE_PERFORMED_SERVICE_PAYMENT)
                .setConverter(context -> applyChanges(context.getSource(), new PerformedServicePayment(), modelMapper));

        modelMapper.typeMap(PerformedServicePaymentTO.class, PerformedServicePayment.class, UPDATE_PERFORMED_SERVICE_PAYMENT)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination(), modelMapper));

        modelMapper.typeMap(PiggyBankTO.class, PiggyBank.class, CREATE_PIGGY_BANK)
                .setConverter(context -> applyChanges(context.getSource(), new PiggyBank()));

        modelMapper.typeMap(PiggyBankTO.class, PiggyBank.class, UPDATE_PIGGY_BANK)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(ServiceTO.class, Service.class, CREATE_SERVICE)
                .setConverter(context -> applyChanges(context.getSource(), new Service()));

        modelMapper.typeMap(ServiceTO.class, Service.class, UPDATE_SERVICE)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(CubeRecordTO.class, CubeRecord.class, CREATE_CUBE_RECORD)
                .setConverter(context -> applyChanges(context.getSource(), new CubeRecord()));

        modelMapper.typeMap(CubeRecordTO.class, CubeRecord.class, UPDATE_CUBE_RECORD)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(CubeRecord.class, CubeRecordTO.class)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(HolidayCurrenciesTO.class, HolidayCurrencies.class, CREATE_HOLIDAY_CURRENCIES)
                .setConverter(context -> applyChanges(context.getSource(), new HolidayCurrencies()));

        modelMapper.typeMap(HolidayCurrenciesTO.class, HolidayCurrencies.class, UPDATE_HOLIDAY_CURRENCIES)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(NodrigenBankPermission.class, NodrigenBankPermissionTO.class);
        modelMapper.typeMap(BankAccount.class, BankAccountTO.class);
        modelMapper.typeMap(NodrigenTransactionsToImport.class, NodrigenTransactionsToImportTO.class);
        modelMapper.typeMap(FinancialTransaction.class, FinancialTransactionTO.class);

        return modelMapper;
    }

    private <T> SourceGetter<PerformedServicePayment> getPropertyOfPSP(Function<ClientPayment, T> mapper, T defaultValue) {
        return psp -> Optional.ofNullable(psp)
                .map(PerformedServicePayment::getClientPayment)
                .map(mapper)
                .orElse(defaultValue);
    }

    private Account applyChanges(AccountTO source, Account destination) {
        destination.setName(source.getName());
        destination.setCurrency(source.getCurrency());
        destination.setVisible(source.isVisible());
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

    private PerformedService applyChanges(PerformedServiceTO source, PerformedService destination, ModelMapper modelMapper) {
        destination.setDate(source.getDate());
        destination.setPrice(source.getPrice());
        destination.setCurrency(source.getCurrency());
        if (source.getClient() != null) {
            destination.setClient(modelMapper.map(source.getClient(), Client.class));
        }
        if (source.getService() != null) {
            destination.setService(modelMapper.map(source.getService(), Service.class));
        }
        return destination;
    }

    private PerformedService applyChanges(PerformedServiceTO source, PerformedService destination) {
        destination.setDate(source.getDate());
        destination.setPrice(source.getPrice());
        return destination;
    }

    private PerformedServicePayment applyChanges(PerformedServicePaymentTO source, PerformedServicePayment destination, ModelMapper modelMapper) {
        PerformedServiceTO performedService = source.getPerformedService();
        if (performedService != null) {
            destination.setPerformedService(modelMapper.map(performedService, PerformedService.class));
        }
        ClientPaymentTO clientPayment = source.getClientPayment();
        if (clientPayment != null) {
            destination.setClientPayment(modelMapper.map(clientPayment, ClientPayment.class));
        }
        destination.setPrice(source.getPrice());
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

    private Service applyChanges(ServiceTO source, Service destination) {
        destination.setName(source.getName());
        return destination;
    }

    private ClientPayment applyChanges(ClientPaymentTO source, ModelMapper modelMapper) {
        ClientPayment destination = applyChanges(source, new ClientPayment());
        if (source.getClient() != null) {
            destination.setClient(modelMapper.map(source.getClient(), Client.class));
        }
        destination.setPrice(source.getPrice());
        destination.setCurrency(source.getCurrency());
        return destination;
    }

    private ClientPayment applyChanges(ClientPaymentTO source, ClientPayment destination) {
        destination.setDate(source.getDate());
        destination.setBillOfSale(source.isBillOfSale());
        destination.setBillOfSaleAsInvoice(source.isBillOfSaleAsInvoice());
        destination.setInvoice(source.isInvoice());
        destination.setNotRegistered(source.isNotRegistered());
        return destination;
    }

    private CubeRecord applyChanges(CubeRecordTO source, CubeRecord destination) {
        return destination.setCubesType(source.getCubesType())
                .setScramble(source.getScramble())
                .setTime(Duration.ofMillis((long) (source.getTime() * 1000)))
                .setRecordTime(source.getRecordTime());
    }

    private CubeRecordTO applyChanges(CubeRecord source, CubeRecordTO destination) {
        return (destination == null ? new CubeRecordTO() : destination).setCubesType(source.getCubesType())
                .setScramble(source.getScramble())
                .setTime(source.getTime() == null ? 0 : ((double) (source.getTime().toMillis())) / 1000.0)
                .setDomain(source.getDomain() == null
                        ? null
                        : new DomainTO()
                        .setId(source.getDomain().getId())
                        .setName(source.getDomain().getName()))
                .setRecordTime(source.getRecordTime());
    }

    private HolidayCurrencies applyChanges(HolidayCurrenciesTO source, HolidayCurrencies destination) {
        return destination
                .setEuroConversionRate(source.getEuroConversionRate())
                .setKunaConversionRate(source.getKunaConversionRate());
    }

    @Bean
    public Gson deserializer() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateJsonDeserializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeJsonDeserializer())
                .create();
    }
}
