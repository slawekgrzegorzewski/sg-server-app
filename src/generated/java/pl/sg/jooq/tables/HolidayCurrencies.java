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
public class HolidayCurrencies extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.holiday_currencies</code>
     */
    public static final HolidayCurrencies HOLIDAY_CURRENCIES = new HolidayCurrencies();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.holiday_currencies.id</code>.
     */
    public final TableField<Record, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.holiday_currencies.euro_conversion_rate</code>.
     */
    public final TableField<Record, BigDecimal> EURO_CONVERSION_RATE = createField(DSL.name("euro_conversion_rate"), SQLDataType.NUMERIC(19, 6), this, "");

    /**
     * The column <code>public.holiday_currencies.kuna_conversion_rate</code>.
     */
    public final TableField<Record, BigDecimal> KUNA_CONVERSION_RATE = createField(DSL.name("kuna_conversion_rate"), SQLDataType.NUMERIC(19, 6), this, "");

    /**
     * The column <code>public.holiday_currencies.domain_id</code>.
     */
    public final TableField<Record, Integer> DOMAIN_ID = createField(DSL.name("domain_id"), SQLDataType.INTEGER, this, "");

    private HolidayCurrencies(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private HolidayCurrencies(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.holiday_currencies</code> table reference
     */
    public HolidayCurrencies(String alias) {
        this(DSL.name(alias), HOLIDAY_CURRENCIES);
    }

    /**
     * Create an aliased <code>public.holiday_currencies</code> table reference
     */
    public HolidayCurrencies(Name alias) {
        this(alias, HOLIDAY_CURRENCIES);
    }

    /**
     * Create a <code>public.holiday_currencies</code> table reference
     */
    public HolidayCurrencies() {
        this(DSL.name("holiday_currencies"), null);
    }

    public <O extends Record> HolidayCurrencies(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, HOLIDAY_CURRENCIES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.HOLIDAY_CURRENCIES_PKEY;
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.asList(Keys.HOLIDAY_CURRENCIES__FK_DOMAIN);
    }

    private transient Domain _domain;

    /**
     * Get the implicit join path to the <code>public.domain</code> table.
     */
    public Domain domain() {
        if (_domain == null)
            _domain = new Domain(this, Keys.HOLIDAY_CURRENCIES__FK_DOMAIN);

        return _domain;
    }

    @Override
    public HolidayCurrencies as(String alias) {
        return new HolidayCurrencies(DSL.name(alias), this);
    }

    @Override
    public HolidayCurrencies as(Name alias) {
        return new HolidayCurrencies(alias, this);
    }

    @Override
    public HolidayCurrencies as(Table<?> alias) {
        return new HolidayCurrencies(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public HolidayCurrencies rename(String name) {
        return new HolidayCurrencies(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public HolidayCurrencies rename(Name name) {
        return new HolidayCurrencies(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public HolidayCurrencies rename(Table<?> name) {
        return new HolidayCurrencies(name.getQualifiedName(), null);
    }
}