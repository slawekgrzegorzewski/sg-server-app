/*
 * This file is generated by jOOQ.
 */
package pl.sg.jooq.tables;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import pl.sg.jooq.Keys;
import pl.sg.jooq.Public;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class NodrigenBankAccountBalance extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of
     * <code>public.nodrigen_bank_account_balance</code>
     */
    public static final NodrigenBankAccountBalance NODRIGEN_BANK_ACCOUNT_BALANCE = new NodrigenBankAccountBalance();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.nodrigen_bank_account_balance.id</code>.
     */
    public final TableField<Record, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column
     * <code>public.nodrigen_bank_account_balance.balance_amount_amount</code>.
     */
    public final TableField<Record, BigDecimal> BALANCE_AMOUNT_AMOUNT = createField(DSL.name("balance_amount_amount"), SQLDataType.NUMERIC(19, 2), this, "");

    /**
     * The column
     * <code>public.nodrigen_bank_account_balance.balance_amount_currency</code>.
     */
    public final TableField<Record, String> BALANCE_AMOUNT_CURRENCY = createField(DSL.name("balance_amount_currency"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>public.nodrigen_bank_account_balance.balance_type</code>.
     */
    public final TableField<Record, String> BALANCE_TYPE = createField(DSL.name("balance_type"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>public.nodrigen_bank_account_balance.credit_limit_included</code>.
     */
    public final TableField<Record, Boolean> CREDIT_LIMIT_INCLUDED = createField(DSL.name("credit_limit_included"), SQLDataType.BOOLEAN, this, "");

    /**
     * The column <code>public.nodrigen_bank_account_balance.fetch_time</code>.
     */
    public final TableField<Record, LocalDateTime> FETCH_TIME = createField(DSL.name("fetch_time"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column
     * <code>public.nodrigen_bank_account_balance.last_change_date_time</code>.
     */
    public final TableField<Record, LocalDateTime> LAST_CHANGE_DATE_TIME = createField(DSL.name("last_change_date_time"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column
     * <code>public.nodrigen_bank_account_balance.last_committed_transaction</code>.
     */
    public final TableField<Record, String> LAST_COMMITTED_TRANSACTION = createField(DSL.name("last_committed_transaction"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column
     * <code>public.nodrigen_bank_account_balance.reference_date</code>.
     */
    public final TableField<Record, LocalDate> REFERENCE_DATE = createField(DSL.name("reference_date"), SQLDataType.LOCALDATE, this, "");

    /**
     * The column
     * <code>public.nodrigen_bank_account_balance.bank_account_id</code>.
     */
    public final TableField<Record, Integer> BANK_ACCOUNT_ID = createField(DSL.name("bank_account_id"), SQLDataType.INTEGER, this, "");

    private NodrigenBankAccountBalance(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private NodrigenBankAccountBalance(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.nodrigen_bank_account_balance</code> table
     * reference
     */
    public NodrigenBankAccountBalance(String alias) {
        this(DSL.name(alias), NODRIGEN_BANK_ACCOUNT_BALANCE);
    }

    /**
     * Create an aliased <code>public.nodrigen_bank_account_balance</code> table
     * reference
     */
    public NodrigenBankAccountBalance(Name alias) {
        this(alias, NODRIGEN_BANK_ACCOUNT_BALANCE);
    }

    /**
     * Create a <code>public.nodrigen_bank_account_balance</code> table
     * reference
     */
    public NodrigenBankAccountBalance() {
        this(DSL.name("nodrigen_bank_account_balance"), null);
    }

    public <O extends Record> NodrigenBankAccountBalance(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, NODRIGEN_BANK_ACCOUNT_BALANCE);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.NODRIGEN_BANK_ACCOUNT_BALANCE_PKEY;
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.asList(Keys.NODRIGEN_BANK_ACCOUNT_BALANCE__FK_BANK_ACCOUNT);
    }

    private transient BankAccount _bankAccount;

    /**
     * Get the implicit join path to the <code>public.bank_account</code> table.
     */
    public BankAccount bankAccount() {
        if (_bankAccount == null)
            _bankAccount = new BankAccount(this, Keys.NODRIGEN_BANK_ACCOUNT_BALANCE__FK_BANK_ACCOUNT);

        return _bankAccount;
    }

    @Override
    public NodrigenBankAccountBalance as(String alias) {
        return new NodrigenBankAccountBalance(DSL.name(alias), this);
    }

    @Override
    public NodrigenBankAccountBalance as(Name alias) {
        return new NodrigenBankAccountBalance(alias, this);
    }

    @Override
    public NodrigenBankAccountBalance as(Table<?> alias) {
        return new NodrigenBankAccountBalance(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public NodrigenBankAccountBalance rename(String name) {
        return new NodrigenBankAccountBalance(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public NodrigenBankAccountBalance rename(Name name) {
        return new NodrigenBankAccountBalance(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public NodrigenBankAccountBalance rename(Table<?> name) {
        return new NodrigenBankAccountBalance(name.getQualifiedName(), null);
    }
}