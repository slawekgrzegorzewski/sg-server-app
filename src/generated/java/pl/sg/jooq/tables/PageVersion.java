/*
 * This file is generated by jOOQ.
 */
package pl.sg.jooq.tables;


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
public class PageVersion extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.page_version</code>
     */
    public static final PageVersion PAGE_VERSION = new PageVersion();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.page_version.id</code>.
     */
    public final TableField<Record, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.page_version.version_time</code>.
     */
    public final TableField<Record, LocalDateTime> VERSION_TIME = createField(DSL.name("version_time"), SQLDataType.LOCALDATETIME(6), this, "");

    /**
     * The column <code>public.page_version.task_id</code>.
     */
    public final TableField<Record, Integer> TASK_ID = createField(DSL.name("task_id"), SQLDataType.INTEGER, this, "");

    private PageVersion(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private PageVersion(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.page_version</code> table reference
     */
    public PageVersion(String alias) {
        this(DSL.name(alias), PAGE_VERSION);
    }

    /**
     * Create an aliased <code>public.page_version</code> table reference
     */
    public PageVersion(Name alias) {
        this(alias, PAGE_VERSION);
    }

    /**
     * Create a <code>public.page_version</code> table reference
     */
    public PageVersion() {
        this(DSL.name("page_version"), null);
    }

    public <O extends Record> PageVersion(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, PAGE_VERSION);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.PAGE_VERSION_PKEY;
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.asList(Keys.PAGE_VERSION__FK_CHECKER_TASK);
    }

    private transient CheckerTask _checkerTask;

    /**
     * Get the implicit join path to the <code>public.checker_task</code> table.
     */
    public CheckerTask checkerTask() {
        if (_checkerTask == null)
            _checkerTask = new CheckerTask(this, Keys.PAGE_VERSION__FK_CHECKER_TASK);

        return _checkerTask;
    }

    @Override
    public PageVersion as(String alias) {
        return new PageVersion(DSL.name(alias), this);
    }

    @Override
    public PageVersion as(Name alias) {
        return new PageVersion(alias, this);
    }

    @Override
    public PageVersion as(Table<?> alias) {
        return new PageVersion(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PageVersion rename(String name) {
        return new PageVersion(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PageVersion rename(Name name) {
        return new PageVersion(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PageVersion rename(Table<?> name) {
        return new PageVersion(name.getQualifiedName(), null);
    }
}