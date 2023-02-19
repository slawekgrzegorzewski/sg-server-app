package pl.sg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.SourceGetter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.sg.accountant.model.accounts.FinancialTransaction;
import pl.sg.accountant.model.accounts.PerformedServicePayment;
import pl.sg.accountant.transport.FinancialTransactionTO;
import pl.sg.accountant.transport.HolidayCurrencies;
import pl.sg.accountant.transport.PiggyBank;
import pl.sg.accountant.transport.accounts.*;
import pl.sg.accountant.transport.billings.BillingPeriod;
import pl.sg.accountant.transport.billings.Category;
import pl.sg.application.api.DomainSimple;
import pl.sg.banks.transport.BankAccount;
import pl.sg.cubes.transport.CubeRecord;
import pl.sg.integrations.nodrigen.model.NodrigenTransactionsToImport;
import pl.sg.integrations.nodrigen.transport.NodrigenBankPermission;
import pl.sg.integrations.nodrigen.transport.NodrigenTransactionsToImportTO;
import pl.sg.ip.api.TimeRecord;
import pl.sg.ip.model.IntellectualProperty;
import pl.sg.ip.model.Task;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        modelMapper.typeMap(pl.sg.accountant.model.accounts.Account.class, Account.class);
        modelMapper.typeMap(pl.sg.accountant.model.billings.BillingPeriod.class, BillingPeriod.class);
        modelMapper.typeMap(pl.sg.accountant.model.billings.PiggyBank.class, PiggyBank.class);
        modelMapper.typeMap(pl.sg.accountant.model.accounts.ClientPayment.class, ClientPayment.class)
                .addMapping(pl.sg.accountant.model.accounts.ClientPayment::getServices, ClientPayment::setServiceRelations);
        modelMapper.typeMap(pl.sg.accountant.model.accounts.PerformedService.class, PerformedService.class)
                .addMapping(pl.sg.accountant.model.accounts.PerformedService::getPayments, PerformedService::setClientPaymentsRelations);

        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::getDate, null), PerformedServicePaymentTO::setDate);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::getCurrency, null), PerformedServicePaymentTO::setCurrency);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::isInvoice, false), PerformedServicePaymentTO::setInvoice);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::isBillOfSale, false), PerformedServicePaymentTO::setBillOfSale);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::isBillOfSaleAsInvoice, false), PerformedServicePaymentTO::setBillOfSaleAsInvoice);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::isNotRegistered, false), PerformedServicePaymentTO::setNotRegistered);

        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::getDate, null), PerformedServicePaymentSimpleTO::setDate);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::getCurrency, null), PerformedServicePaymentSimpleTO::setCurrency);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::isInvoice, false), PerformedServicePaymentSimpleTO::setInvoice);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::isBillOfSale, false), PerformedServicePaymentSimpleTO::setBillOfSale);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::isBillOfSaleAsInvoice, false), PerformedServicePaymentSimpleTO::setBillOfSaleAsInvoice);
        modelMapper.typeMap(PerformedServicePayment.class, PerformedServicePaymentSimpleTO.class)
                .addMapping(getPropertyOfPSP(pl.sg.accountant.model.accounts.ClientPayment::isNotRegistered, false), PerformedServicePaymentSimpleTO::setNotRegistered);

        modelMapper.typeMap(Account.class, pl.sg.accountant.model.accounts.Account.class, CREATE_ACCOUNT)
                .setConverter(context -> applyChanges(context.getSource(), new pl.sg.accountant.model.accounts.Account()));

        modelMapper.typeMap(Account.class, pl.sg.accountant.model.accounts.Account.class, UPDATE_ACCOUNT)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(Category.class, pl.sg.accountant.model.billings.Category.class, CREATE_CATEGORY)
                .setConverter(context -> applyChanges(context.getSource(), new pl.sg.accountant.model.billings.Category()));

        modelMapper.typeMap(Category.class, pl.sg.accountant.model.billings.Category.class, UPDATE_CATEGORY)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(Client.class, pl.sg.accountant.model.accounts.Client.class, CREATE_CLIENT)
                .setConverter(context -> applyChanges(context.getSource(), new pl.sg.accountant.model.accounts.Client()));

        modelMapper.typeMap(Client.class, pl.sg.accountant.model.accounts.Client.class, UPDATE_CLIENT)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(ClientPayment.class, pl.sg.accountant.model.accounts.ClientPayment.class, CREATE_CLIENT_PAYMENT)
                .setConverter(context -> applyChanges(context.getSource(), modelMapper));

        modelMapper.typeMap(ClientPayment.class, pl.sg.accountant.model.accounts.ClientPayment.class, UPDATE_CLIENT_PAYMENT)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(PerformedService.class, pl.sg.accountant.model.accounts.PerformedService.class, CREATE_PERFORMED_SERVICE)
                .setConverter(context -> applyChanges(context.getSource(), new pl.sg.accountant.model.accounts.PerformedService(), modelMapper));

        modelMapper.typeMap(PerformedService.class, pl.sg.accountant.model.accounts.PerformedService.class, UPDATE_PERFORMED_SERVICE)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(PerformedServicePaymentTO.class, PerformedServicePayment.class, CREATE_PERFORMED_SERVICE_PAYMENT)
                .setConverter(context -> applyChanges(context.getSource(), new PerformedServicePayment(), modelMapper));

        modelMapper.typeMap(PerformedServicePaymentTO.class, PerformedServicePayment.class, UPDATE_PERFORMED_SERVICE_PAYMENT)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination(), modelMapper));

        modelMapper.typeMap(PiggyBank.class, pl.sg.accountant.model.billings.PiggyBank.class, CREATE_PIGGY_BANK)
                .setConverter(context -> applyChanges(context.getSource(), new pl.sg.accountant.model.billings.PiggyBank()));

        modelMapper.typeMap(PiggyBank.class, pl.sg.accountant.model.billings.PiggyBank.class, UPDATE_PIGGY_BANK)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(Service.class, pl.sg.accountant.model.accounts.Service.class, CREATE_SERVICE)
                .setConverter(context -> applyChanges(context.getSource(), new pl.sg.accountant.model.accounts.Service()));

        modelMapper.typeMap(Service.class, pl.sg.accountant.model.accounts.Service.class, UPDATE_SERVICE)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(CubeRecord.class, pl.sg.cubes.model.CubeRecord.class, CREATE_CUBE_RECORD)
                .setConverter(context -> applyChanges(context.getSource(), new pl.sg.cubes.model.CubeRecord()));

        modelMapper.typeMap(CubeRecord.class, pl.sg.cubes.model.CubeRecord.class, UPDATE_CUBE_RECORD)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(pl.sg.cubes.model.CubeRecord.class, CubeRecord.class)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(HolidayCurrencies.class, pl.sg.accountant.model.HolidayCurrencies.class, CREATE_HOLIDAY_CURRENCIES)
                .setConverter(context -> applyChanges(context.getSource(), new pl.sg.accountant.model.HolidayCurrencies()));

        modelMapper.typeMap(HolidayCurrencies.class, pl.sg.accountant.model.HolidayCurrencies.class, UPDATE_HOLIDAY_CURRENCIES)
                .setConverter(context -> applyChanges(context.getSource(), context.getDestination()));

        modelMapper.typeMap(pl.sg.integrations.nodrigen.model.NodrigenBankPermission.class, NodrigenBankPermission.class);
        modelMapper.typeMap(pl.sg.banks.model.BankAccount.class, BankAccount.class);
        modelMapper.typeMap(NodrigenTransactionsToImport.class, NodrigenTransactionsToImportTO.class);
        modelMapper.typeMap(FinancialTransaction.class, FinancialTransactionTO.class);
        modelMapper.typeMap(IntellectualProperty.class, pl.sg.ip.api.IntellectualProperty.class)
                .addMapping(IntellectualProperty::tasks, pl.sg.ip.api.IntellectualProperty::setTasks);
        modelMapper.typeMap(Task.class, pl.sg.ip.api.Task.class)
                .addMapping(Task::getTimeRecords, pl.sg.ip.api.Task::setTimeRecords);
        modelMapper.typeMap(TimeRecord.class, pl.sg.ip.api.TimeRecord.class);

        return modelMapper;
    }

    private <T> SourceGetter<PerformedServicePayment> getPropertyOfPSP(Function<pl.sg.accountant.model.accounts.ClientPayment, T> mapper, T defaultValue) {
        return psp -> Optional.ofNullable(psp)
                .map(PerformedServicePayment::getClientPayment)
                .map(mapper)
                .orElse(defaultValue);
    }

    private pl.sg.accountant.model.accounts.Account applyChanges(Account source, pl.sg.accountant.model.accounts.Account destination) {
        destination.setName(source.getName());
        destination.setCurrency(source.getCurrency());
        destination.setVisible(source.isVisible());
        return destination;
    }

    private pl.sg.accountant.model.billings.Category applyChanges(Category source, pl.sg.accountant.model.billings.Category destination) {
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        return destination;
    }

    private pl.sg.accountant.model.accounts.Client applyChanges(Client source, pl.sg.accountant.model.accounts.Client destination) {
        destination.setName(source.getName());
        return destination;
    }

    private pl.sg.accountant.model.accounts.PerformedService applyChanges(PerformedService source, pl.sg.accountant.model.accounts.PerformedService destination, ModelMapper modelMapper) {
        destination.setDate(source.getDate());
        destination.setPrice(source.getPrice());
        destination.setCurrency(source.getCurrency());
        if (source.getClient() != null) {
            destination.setClient(modelMapper.map(source.getClient(), pl.sg.accountant.model.accounts.Client.class));
        }
        if (source.getService() != null) {
            destination.setService(modelMapper.map(source.getService(), pl.sg.accountant.model.accounts.Service.class));
        }
        return destination;
    }

    private pl.sg.accountant.model.accounts.PerformedService applyChanges(PerformedService source, pl.sg.accountant.model.accounts.PerformedService destination) {
        destination.setDate(source.getDate());
        destination.setPrice(source.getPrice());
        return destination;
    }

    private PerformedServicePayment applyChanges(PerformedServicePaymentTO source, PerformedServicePayment destination, ModelMapper modelMapper) {
        PerformedService performedService = source.getPerformedService();
        if (performedService != null) {
            destination.setPerformedService(modelMapper.map(performedService, pl.sg.accountant.model.accounts.PerformedService.class));
        }
        ClientPayment clientPayment = source.getClientPayment();
        if (clientPayment != null) {
            destination.setClientPayment(modelMapper.map(clientPayment, pl.sg.accountant.model.accounts.ClientPayment.class));
        }
        destination.setPrice(source.getPrice());
        return destination;
    }

    private pl.sg.accountant.model.billings.PiggyBank applyChanges(PiggyBank source, pl.sg.accountant.model.billings.PiggyBank destination) {
        destination.setName(source.getName());
        destination.setDescription(source.getDescription());
        destination.setBalance(source.getBalance());
        destination.setCurrency(source.getCurrency());
        destination.setMonthlyTopUp(source.getMonthlyTopUp());
        destination.setSavings(source.isSavings());
        return destination;
    }

    private pl.sg.accountant.model.accounts.Service applyChanges(Service source, pl.sg.accountant.model.accounts.Service destination) {
        destination.setName(source.getName());
        return destination;
    }

    private pl.sg.accountant.model.accounts.ClientPayment applyChanges(ClientPayment source, ModelMapper modelMapper) {
        pl.sg.accountant.model.accounts.ClientPayment destination = applyChanges(source, new pl.sg.accountant.model.accounts.ClientPayment());
        if (source.getClient() != null) {
            destination.setClient(modelMapper.map(source.getClient(), pl.sg.accountant.model.accounts.Client.class));
        }
        destination.setPrice(source.getPrice());
        destination.setCurrency(source.getCurrency());
        return destination;
    }

    private pl.sg.accountant.model.accounts.ClientPayment applyChanges(ClientPayment source, pl.sg.accountant.model.accounts.ClientPayment destination) {
        destination.setDate(source.getDate());
        destination.setBillOfSale(source.isBillOfSale());
        destination.setBillOfSaleAsInvoice(source.isBillOfSaleAsInvoice());
        destination.setInvoice(source.isInvoice());
        destination.setNotRegistered(source.isNotRegistered());
        return destination;
    }

    private pl.sg.cubes.model.CubeRecord applyChanges(CubeRecord source, pl.sg.cubes.model.CubeRecord destination) {
        return destination.setCubesType(source.getCubesType())
                .setScramble(source.getScramble())
                .setTime(Duration.ofMillis((long) (source.getTime() * 1000)))
                .setRecordTime(source.getRecordTime());
    }

    private CubeRecord applyChanges(pl.sg.cubes.model.CubeRecord source, CubeRecord destination) {
        return (destination == null ? new CubeRecord() : destination).setCubesType(source.getCubesType())
                .setScramble(source.getScramble())
                .setTime(source.getTime() == null ? 0 : ((double) (source.getTime().toMillis())) / 1000.0)
                .setDomain(source.getDomain() == null
                        ? null
                        : new DomainSimple()
                        .setId(source.getDomain().getId())
                        .setName(source.getDomain().getName()))
                .setRecordTime(source.getRecordTime());
    }

    private pl.sg.accountant.model.HolidayCurrencies applyChanges(HolidayCurrencies source, pl.sg.accountant.model.HolidayCurrencies destination) {
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

//    @Bean(name = "multipartResolver")
//    public StandardServletMultipartResolver multipartResolver() {
//        return new StandardServletMultipartResolver();
//    }

//    @Bean(name = "multipartResolver")
//    public MultipartResolver multipartResolver() {
//        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
//        resolver.setResolveLazily(true);
//        return resolver;
//    }

}
