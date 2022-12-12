/*
 * This file is generated by jOOQ.
 */
package pl.sg.jooq;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Sequence;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import pl.sg.jooq.tables.Account;
import pl.sg.jooq.tables.AccountantSettings;
import pl.sg.jooq.tables.ApplicationUser;
import pl.sg.jooq.tables.ApplicationUserDomainRelation;
import pl.sg.jooq.tables.ApplicationUserRoles;
import pl.sg.jooq.tables.BankAccount;
import pl.sg.jooq.tables.BankPermission;
import pl.sg.jooq.tables.BillingPeriod;
import pl.sg.jooq.tables.Category;
import pl.sg.jooq.tables.CheckerStep;
import pl.sg.jooq.tables.CheckerTask;
import pl.sg.jooq.tables.CheckerTaskHistory;
import pl.sg.jooq.tables.CheckerTaskSteps;
import pl.sg.jooq.tables.Client;
import pl.sg.jooq.tables.ClientPayment;
import pl.sg.jooq.tables.Country;
import pl.sg.jooq.tables.CountryNames;
import pl.sg.jooq.tables.CubeRecord;
import pl.sg.jooq.tables.Domain;
import pl.sg.jooq.tables.DomainInvitation;
import pl.sg.jooq.tables.Expense;
import pl.sg.jooq.tables.FinancialTransaction;
import pl.sg.jooq.tables.FlywaySchemaHistory;
import pl.sg.jooq.tables.HolidayCurrencies;
import pl.sg.jooq.tables.Income;
import pl.sg.jooq.tables.IntellectualProperty;
import pl.sg.jooq.tables.KpirEntry;
import pl.sg.jooq.tables.MonthSummary;
import pl.sg.jooq.tables.NodrigenAccess;
import pl.sg.jooq.tables.NodrigenBankAccountBalance;
import pl.sg.jooq.tables.NodrigenTransaction;
import pl.sg.jooq.tables.NodrigenTransactionsToImport;
import pl.sg.jooq.tables.PageVersion;
import pl.sg.jooq.tables.PageVersionContent;
import pl.sg.jooq.tables.PageVersionElementsAdded;
import pl.sg.jooq.tables.PageVersionElementsRemoved;
import pl.sg.jooq.tables.PerformedService;
import pl.sg.jooq.tables.PerformedServicePayment;
import pl.sg.jooq.tables.PiggyBank;
import pl.sg.jooq.tables.SaveResultStepEmailCcs;
import pl.sg.jooq.tables.SaveResultStepEmailTos;
import pl.sg.jooq.tables.Service;
import pl.sg.jooq.tables.Syr;
import pl.sg.jooq.tables.Task;
import pl.sg.jooq.tables.TaskAttachments;
import pl.sg.jooq.tables.TimeRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.account</code>.
     */
    public final Account ACCOUNT = Account.ACCOUNT;

    /**
     * The table <code>public.accountant_settings</code>.
     */
    public final AccountantSettings ACCOUNTANT_SETTINGS = AccountantSettings.ACCOUNTANT_SETTINGS;

    /**
     * The table <code>public.application_user</code>.
     */
    public final ApplicationUser APPLICATION_USER = ApplicationUser.APPLICATION_USER;

    /**
     * The table <code>public.application_user_domain_relation</code>.
     */
    public final ApplicationUserDomainRelation APPLICATION_USER_DOMAIN_RELATION = ApplicationUserDomainRelation.APPLICATION_USER_DOMAIN_RELATION;

    /**
     * The table <code>public.application_user_roles</code>.
     */
    public final ApplicationUserRoles APPLICATION_USER_ROLES = ApplicationUserRoles.APPLICATION_USER_ROLES;

    /**
     * The table <code>public.bank_account</code>.
     */
    public final BankAccount BANK_ACCOUNT = BankAccount.BANK_ACCOUNT;

    /**
     * The table <code>public.bank_permission</code>.
     */
    public final BankPermission BANK_PERMISSION = BankPermission.BANK_PERMISSION;

    /**
     * The table <code>public.billing_period</code>.
     */
    public final BillingPeriod BILLING_PERIOD = BillingPeriod.BILLING_PERIOD;

    /**
     * The table <code>public.category</code>.
     */
    public final Category CATEGORY = Category.CATEGORY;

    /**
     * The table <code>public.checker_step</code>.
     */
    public final CheckerStep CHECKER_STEP = CheckerStep.CHECKER_STEP;

    /**
     * The table <code>public.checker_task</code>.
     */
    public final CheckerTask CHECKER_TASK = CheckerTask.CHECKER_TASK;

    /**
     * The table <code>public.checker_task_history</code>.
     */
    public final CheckerTaskHistory CHECKER_TASK_HISTORY = CheckerTaskHistory.CHECKER_TASK_HISTORY;

    /**
     * The table <code>public.checker_task_steps</code>.
     */
    public final CheckerTaskSteps CHECKER_TASK_STEPS = CheckerTaskSteps.CHECKER_TASK_STEPS;

    /**
     * The table <code>public.client</code>.
     */
    public final Client CLIENT = Client.CLIENT;

    /**
     * The table <code>public.client_payment</code>.
     */
    public final ClientPayment CLIENT_PAYMENT = ClientPayment.CLIENT_PAYMENT;

    /**
     * The table <code>public.country</code>.
     */
    public final Country COUNTRY = Country.COUNTRY;

    /**
     * The table <code>public.country_names</code>.
     */
    public final CountryNames COUNTRY_NAMES = CountryNames.COUNTRY_NAMES;

    /**
     * The table <code>public.cube_record</code>.
     */
    public final CubeRecord CUBE_RECORD = CubeRecord.CUBE_RECORD;

    /**
     * The table <code>public.domain</code>.
     */
    public final Domain DOMAIN = Domain.DOMAIN;

    /**
     * The table <code>public.domain_invitation</code>.
     */
    public final DomainInvitation DOMAIN_INVITATION = DomainInvitation.DOMAIN_INVITATION;

    /**
     * The table <code>public.expense</code>.
     */
    public final Expense EXPENSE = Expense.EXPENSE;

    /**
     * The table <code>public.financial_transaction</code>.
     */
    public final FinancialTransaction FINANCIAL_TRANSACTION = FinancialTransaction.FINANCIAL_TRANSACTION;

    /**
     * The table <code>public.flyway_schema_history</code>.
     */
    public final FlywaySchemaHistory FLYWAY_SCHEMA_HISTORY = FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY;

    /**
     * The table <code>public.holiday_currencies</code>.
     */
    public final HolidayCurrencies HOLIDAY_CURRENCIES = HolidayCurrencies.HOLIDAY_CURRENCIES;

    /**
     * The table <code>public.income</code>.
     */
    public final Income INCOME = Income.INCOME;

    /**
     * The table <code>public.intellectual_property</code>.
     */
    public final IntellectualProperty INTELLECTUAL_PROPERTY = IntellectualProperty.INTELLECTUAL_PROPERTY;

    /**
     * The table <code>public.kpir_entry</code>.
     */
    public final KpirEntry KPIR_ENTRY = KpirEntry.KPIR_ENTRY;

    /**
     * The table <code>public.month_summary</code>.
     */
    public final MonthSummary MONTH_SUMMARY = MonthSummary.MONTH_SUMMARY;

    /**
     * The table <code>public.nodrigen_access</code>.
     */
    public final NodrigenAccess NODRIGEN_ACCESS = NodrigenAccess.NODRIGEN_ACCESS;

    /**
     * The table <code>public.nodrigen_bank_account_balance</code>.
     */
    public final NodrigenBankAccountBalance NODRIGEN_BANK_ACCOUNT_BALANCE = NodrigenBankAccountBalance.NODRIGEN_BANK_ACCOUNT_BALANCE;

    /**
     * The table <code>public.nodrigen_transaction</code>.
     */
    public final NodrigenTransaction NODRIGEN_TRANSACTION = NodrigenTransaction.NODRIGEN_TRANSACTION;

    /**
     * The table <code>public.nodrigen_transactions_to_import</code>.
     */
    public final NodrigenTransactionsToImport NODRIGEN_TRANSACTIONS_TO_IMPORT = NodrigenTransactionsToImport.NODRIGEN_TRANSACTIONS_TO_IMPORT;

    /**
     * The table <code>public.page_version</code>.
     */
    public final PageVersion PAGE_VERSION = PageVersion.PAGE_VERSION;

    /**
     * The table <code>public.page_version_content</code>.
     */
    public final PageVersionContent PAGE_VERSION_CONTENT = PageVersionContent.PAGE_VERSION_CONTENT;

    /**
     * The table <code>public.page_version_elements_added</code>.
     */
    public final PageVersionElementsAdded PAGE_VERSION_ELEMENTS_ADDED = PageVersionElementsAdded.PAGE_VERSION_ELEMENTS_ADDED;

    /**
     * The table <code>public.page_version_elements_removed</code>.
     */
    public final PageVersionElementsRemoved PAGE_VERSION_ELEMENTS_REMOVED = PageVersionElementsRemoved.PAGE_VERSION_ELEMENTS_REMOVED;

    /**
     * The table <code>public.performed_service</code>.
     */
    public final PerformedService PERFORMED_SERVICE = PerformedService.PERFORMED_SERVICE;

    /**
     * The table <code>public.performed_service_payment</code>.
     */
    public final PerformedServicePayment PERFORMED_SERVICE_PAYMENT = PerformedServicePayment.PERFORMED_SERVICE_PAYMENT;

    /**
     * The table <code>public.piggy_bank</code>.
     */
    public final PiggyBank PIGGY_BANK = PiggyBank.PIGGY_BANK;

    /**
     * The table <code>public.save_result_step_email_ccs</code>.
     */
    public final SaveResultStepEmailCcs SAVE_RESULT_STEP_EMAIL_CCS = SaveResultStepEmailCcs.SAVE_RESULT_STEP_EMAIL_CCS;

    /**
     * The table <code>public.save_result_step_email_tos</code>.
     */
    public final SaveResultStepEmailTos SAVE_RESULT_STEP_EMAIL_TOS = SaveResultStepEmailTos.SAVE_RESULT_STEP_EMAIL_TOS;

    /**
     * The table <code>public.service</code>.
     */
    public final Service SERVICE = Service.SERVICE;

    /**
     * The table <code>public.syr</code>.
     */
    public final Syr SYR = Syr.SYR;

    /**
     * The table <code>public.task</code>.
     */
    public final Task TASK = Task.TASK;

    /**
     * The table <code>public.task_attachments</code>.
     */
    public final TaskAttachments TASK_ATTACHMENTS = TaskAttachments.TASK_ATTACHMENTS;

    /**
     * The table <code>public.time_record</code>.
     */
    public final TimeRecord TIME_RECORD = TimeRecord.TIME_RECORD;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Sequence<?>> getSequences() {
        return Arrays.asList(
            Sequences.HIBERNATE_SEQUENCE,
            Sequences.INTELLECTUAL_PROPERTY_ID_SEQUENCE,
            Sequences.INTELLECTUAL_PROPERTY_TASK_ID_SEQUENCE,
            Sequences.NODRIGEN_TRANSACTIONS_TO_IMPORT_SEQUENCE,
            Sequences.TIME_RECORD_ID_SEQUENCE
        );
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Account.ACCOUNT,
            AccountantSettings.ACCOUNTANT_SETTINGS,
            ApplicationUser.APPLICATION_USER,
            ApplicationUserDomainRelation.APPLICATION_USER_DOMAIN_RELATION,
            ApplicationUserRoles.APPLICATION_USER_ROLES,
            BankAccount.BANK_ACCOUNT,
            BankPermission.BANK_PERMISSION,
            BillingPeriod.BILLING_PERIOD,
            Category.CATEGORY,
            CheckerStep.CHECKER_STEP,
            CheckerTask.CHECKER_TASK,
            CheckerTaskHistory.CHECKER_TASK_HISTORY,
            CheckerTaskSteps.CHECKER_TASK_STEPS,
            Client.CLIENT,
            ClientPayment.CLIENT_PAYMENT,
            Country.COUNTRY,
            CountryNames.COUNTRY_NAMES,
            CubeRecord.CUBE_RECORD,
            Domain.DOMAIN,
            DomainInvitation.DOMAIN_INVITATION,
            Expense.EXPENSE,
            FinancialTransaction.FINANCIAL_TRANSACTION,
            FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY,
            HolidayCurrencies.HOLIDAY_CURRENCIES,
            Income.INCOME,
            IntellectualProperty.INTELLECTUAL_PROPERTY,
            KpirEntry.KPIR_ENTRY,
            MonthSummary.MONTH_SUMMARY,
            NodrigenAccess.NODRIGEN_ACCESS,
            NodrigenBankAccountBalance.NODRIGEN_BANK_ACCOUNT_BALANCE,
            NodrigenTransaction.NODRIGEN_TRANSACTION,
            NodrigenTransactionsToImport.NODRIGEN_TRANSACTIONS_TO_IMPORT,
            PageVersion.PAGE_VERSION,
            PageVersionContent.PAGE_VERSION_CONTENT,
            PageVersionElementsAdded.PAGE_VERSION_ELEMENTS_ADDED,
            PageVersionElementsRemoved.PAGE_VERSION_ELEMENTS_REMOVED,
            PerformedService.PERFORMED_SERVICE,
            PerformedServicePayment.PERFORMED_SERVICE_PAYMENT,
            PiggyBank.PIGGY_BANK,
            SaveResultStepEmailCcs.SAVE_RESULT_STEP_EMAIL_CCS,
            SaveResultStepEmailTos.SAVE_RESULT_STEP_EMAIL_TOS,
            Service.SERVICE,
            Syr.SYR,
            Task.TASK,
            TaskAttachments.TASK_ATTACHMENTS,
            TimeRecord.TIME_RECORD
        );
    }
}