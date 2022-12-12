/*
 * This file is generated by jOOQ.
 */
package pl.sg.jooq.tables;


import java.math.BigDecimal;
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
public class Account extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.account</code>
     */
    public static final Account ACCOUNT = new Account();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.account.id</code>.
     */
    public final TableField<Record, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.account.currency</code>.
     */
    public final TableField<Record, String> CURRENCY = createField(DSL.name("currency"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.account.current_balance</code>.
     */
    public final TableField<Record, BigDecimal> CURRENT_BALANCE = createField(DSL.name("current_balance"), SQLDataType.NUMERIC(19, 2).nullable(false).defaultValue(DSL.field("0", SQLDataType.NUMERIC)), this, "");

    /**
     * The column <code>public.account.name</code>.
     */
    public final TableField<Record, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column
     * <code>public.account.last_transaction_included_in_balance_id</code>.
     */
    public final TableField<Record, Integer> LAST_TRANSACTION_INCLUDED_IN_BALANCE_ID = createField(DSL.name("last_transaction_included_in_balance_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.account.domain_id</code>.
     */
    public final TableField<Record, Integer> DOMAIN_ID = createField(DSL.name("domain_id"), SQLDataType.INTEGER, this, "");

    /**
     * The column <code>public.account.visible</code>.
     */
    public final TableField<Record, Boolean> VISIBLE = createField(DSL.name("visible"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("true", SQLDataType.BOOLEAN)), this, "");

    /**
     * The column <code>public.account.bank_account_id</code>.
     */
    public final TableField<Record, Integer> BANK_ACCOUNT_ID = createField(DSL.name("bank_account_id"), SQLDataType.INTEGER, this, "");

    private Account(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private Account(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.account</code> table reference
     */
    public Account(String alias) {
        this(DSL.name(alias), ACCOUNT);
    }

    /**
     * Create an aliased <code>public.account</code> table reference
     */
    public Account(Name alias) {
        this(alias, ACCOUNT);
    }

    /**
     * Create a <code>public.account</code> table reference
     */
    public Account() {
        this(DSL.name("account"), null);
    }

    public <O extends Record> Account(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, ACCOUNT);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.ACCOUNT_PKEY;
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.asList(Keys.ACCOUNT__FK_FINANCIAL_TRANSACTION, Keys.ACCOUNT__FK_DOMAIN, Keys.ACCOUNT__FK_BANK_ACCOUNT);
    }

    private transient FinancialTransaction _financialTransaction;
    private transient Domain _domain;
    private transient BankAccount _bankAccount;

    /**
     * Get the implicit join path to the
     * <code>public.financial_transaction</code> table.
     */
    public FinancialTransaction financialTransaction() {
        if (_financialTransaction == null)
            _financialTransaction = new FinancialTransaction(this, Keys.ACCOUNT__FK_FINANCIAL_TRANSACTION);

        return _financialTransaction;
    }

    /**
     * Get the implicit join path to the <code>public.domain</code> table.
     */
    public Domain domain() {
        if (_domain == null)
            _domain = new Domain(this, Keys.ACCOUNT__FK_DOMAIN);

        return _domain;
    }

    /**
     * Get the implicit join path to the <code>public.bank_account</code> table.
     */
    public BankAccount bankAccount() {
        if (_bankAccount == null)
            _bankAccount = new BankAccount(this, Keys.ACCOUNT__FK_BANK_ACCOUNT);

        return _bankAccount;
    }

    @Override
    public Account as(String alias) {
        return new Account(DSL.name(alias), this);
    }

    @Override
    public Account as(Name alias) {
        return new Account(alias, this);
    }

    @Override
    public Account as(Table<?> alias) {
        return new Account(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Account rename(String name) {
        return new Account(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Account rename(Name name) {
        return new Account(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Account rename(Table<?> name) {
        return new Account(name.getQualifiedName(), null);
    }
}