/*
 * This file is generated by jOOQ.
 */
package pl.sg.jooq.tables;


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
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import pl.sg.jooq.Keys;
import pl.sg.jooq.Public;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CountryNames extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.country_names</code>
     */
    public static final CountryNames COUNTRY_NAMES = new CountryNames();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.country_names.country_id</code>.
     */
    public final TableField<Record, Integer> COUNTRY_ID = createField(DSL.name("country_id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.country_names.names</code>.
     */
    public final TableField<Record, String> NAMES = createField(DSL.name("names"), SQLDataType.VARCHAR(255), this, "");

    private CountryNames(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private CountryNames(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.country_names</code> table reference
     */
    public CountryNames(String alias) {
        this(DSL.name(alias), COUNTRY_NAMES);
    }

    /**
     * Create an aliased <code>public.country_names</code> table reference
     */
    public CountryNames(Name alias) {
        this(alias, COUNTRY_NAMES);
    }

    /**
     * Create a <code>public.country_names</code> table reference
     */
    public CountryNames() {
        this(DSL.name("country_names"), null);
    }

    public <O extends Record> CountryNames(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, COUNTRY_NAMES);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.asList(Keys.COUNTRY_NAMES__FK_COUNTRY);
    }

    private transient Country _country;

    /**
     * Get the implicit join path to the <code>public.country</code> table.
     */
    public Country country() {
        if (_country == null)
            _country = new Country(this, Keys.COUNTRY_NAMES__FK_COUNTRY);

        return _country;
    }

    @Override
    public CountryNames as(String alias) {
        return new CountryNames(DSL.name(alias), this);
    }

    @Override
    public CountryNames as(Name alias) {
        return new CountryNames(alias, this);
    }

    @Override
    public CountryNames as(Table<?> alias) {
        return new CountryNames(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public CountryNames rename(String name) {
        return new CountryNames(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public CountryNames rename(Name name) {
        return new CountryNames(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public CountryNames rename(Table<?> name) {
        return new CountryNames(name.getQualifiedName(), null);
    }
}