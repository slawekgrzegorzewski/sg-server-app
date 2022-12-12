/*
 * This file is generated by jOOQ.
 */
package pl.sg.jooq.tables;


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
public class Domain extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.domain</code>
     */
    public static final Domain DOMAIN = new Domain();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.domain.id</code>.
     */
    public final TableField<Record, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.domain.name</code>.
     */
    public final TableField<Record, String> NAME = createField(DSL.name("name"), SQLDataType.VARCHAR(255), this, "");

    private Domain(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private Domain(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.domain</code> table reference
     */
    public Domain(String alias) {
        this(DSL.name(alias), DOMAIN);
    }

    /**
     * Create an aliased <code>public.domain</code> table reference
     */
    public Domain(Name alias) {
        this(alias, DOMAIN);
    }

    /**
     * Create a <code>public.domain</code> table reference
     */
    public Domain() {
        this(DSL.name("domain"), null);
    }

    public <O extends Record> Domain(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, DOMAIN);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.DOMAIN_PKEY;
    }

    @Override
    public Domain as(String alias) {
        return new Domain(DSL.name(alias), this);
    }

    @Override
    public Domain as(Name alias) {
        return new Domain(alias, this);
    }

    @Override
    public Domain as(Table<?> alias) {
        return new Domain(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Domain rename(String name) {
        return new Domain(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Domain rename(Name name) {
        return new Domain(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Domain rename(Table<?> name) {
        return new Domain(name.getQualifiedName(), null);
    }
}