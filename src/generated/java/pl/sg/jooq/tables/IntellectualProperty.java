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
public class IntellectualProperty extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.intellectual_property</code>
     */
    public static final IntellectualProperty INTELLECTUAL_PROPERTY = new IntellectualProperty();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.intellectual_property.id</code>.
     */
    public final TableField<Record, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.intellectual_property.description</code>.
     */
    public final TableField<Record, String> DESCRIPTION = createField(DSL.name("description"), SQLDataType.VARCHAR(10000), this, "");

    /**
     * The column <code>public.intellectual_property.domain_id</code>.
     */
    public final TableField<Record, Integer> DOMAIN_ID = createField(DSL.name("domain_id"), SQLDataType.INTEGER, this, "");

    private IntellectualProperty(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private IntellectualProperty(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.intellectual_property</code> table
     * reference
     */
    public IntellectualProperty(String alias) {
        this(DSL.name(alias), INTELLECTUAL_PROPERTY);
    }

    /**
     * Create an aliased <code>public.intellectual_property</code> table
     * reference
     */
    public IntellectualProperty(Name alias) {
        this(alias, INTELLECTUAL_PROPERTY);
    }

    /**
     * Create a <code>public.intellectual_property</code> table reference
     */
    public IntellectualProperty() {
        this(DSL.name("intellectual_property"), null);
    }

    public <O extends Record> IntellectualProperty(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, INTELLECTUAL_PROPERTY);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.INTELLECTUAL_PROPERTY_PKEY;
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.asList(Keys.INTELLECTUAL_PROPERTY__FK_DOMAIN);
    }

    private transient Domain _domain;

    /**
     * Get the implicit join path to the <code>public.domain</code> table.
     */
    public Domain domain() {
        if (_domain == null)
            _domain = new Domain(this, Keys.INTELLECTUAL_PROPERTY__FK_DOMAIN);

        return _domain;
    }

    @Override
    public IntellectualProperty as(String alias) {
        return new IntellectualProperty(DSL.name(alias), this);
    }

    @Override
    public IntellectualProperty as(Name alias) {
        return new IntellectualProperty(alias, this);
    }

    @Override
    public IntellectualProperty as(Table<?> alias) {
        return new IntellectualProperty(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public IntellectualProperty rename(String name) {
        return new IntellectualProperty(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public IntellectualProperty rename(Name name) {
        return new IntellectualProperty(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public IntellectualProperty rename(Table<?> name) {
        return new IntellectualProperty(name.getQualifiedName(), null);
    }
}
