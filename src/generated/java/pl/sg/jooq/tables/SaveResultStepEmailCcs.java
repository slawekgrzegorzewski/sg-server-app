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
public class SaveResultStepEmailCcs extends TableImpl<Record> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>public.save_result_step_email_ccs</code>
     */
    public static final SaveResultStepEmailCcs SAVE_RESULT_STEP_EMAIL_CCS = new SaveResultStepEmailCcs();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<Record> getRecordType() {
        return Record.class;
    }

    /**
     * The column <code>public.save_result_step_email_ccs.id</code>.
     */
    public final TableField<Record, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.save_result_step_email_ccs.cc_email</code>.
     */
    public final TableField<Record, String> CC_EMAIL = createField(DSL.name("cc_email"), SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.save_result_step_email_ccs.cc_name</code>.
     */
    public final TableField<Record, String> CC_NAME = createField(DSL.name("cc_name"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    private SaveResultStepEmailCcs(Name alias, Table<Record> aliased) {
        this(alias, aliased, null);
    }

    private SaveResultStepEmailCcs(Name alias, Table<Record> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>public.save_result_step_email_ccs</code> table
     * reference
     */
    public SaveResultStepEmailCcs(String alias) {
        this(DSL.name(alias), SAVE_RESULT_STEP_EMAIL_CCS);
    }

    /**
     * Create an aliased <code>public.save_result_step_email_ccs</code> table
     * reference
     */
    public SaveResultStepEmailCcs(Name alias) {
        this(alias, SAVE_RESULT_STEP_EMAIL_CCS);
    }

    /**
     * Create a <code>public.save_result_step_email_ccs</code> table reference
     */
    public SaveResultStepEmailCcs() {
        this(DSL.name("save_result_step_email_ccs"), null);
    }

    public <O extends Record> SaveResultStepEmailCcs(Table<O> child, ForeignKey<O, Record> key) {
        super(child, key, SAVE_RESULT_STEP_EMAIL_CCS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public UniqueKey<Record> getPrimaryKey() {
        return Keys.SAVE_RESULT_STEP_EMAIL_CCS_PKEY;
    }

    @Override
    public List<ForeignKey<Record, ?>> getReferences() {
        return Arrays.asList(Keys.SAVE_RESULT_STEP_EMAIL_CCS__FK_CHECKER_STEP);
    }

    private transient CheckerStep _checkerStep;

    /**
     * Get the implicit join path to the <code>public.checker_step</code> table.
     */
    public CheckerStep checkerStep() {
        if (_checkerStep == null)
            _checkerStep = new CheckerStep(this, Keys.SAVE_RESULT_STEP_EMAIL_CCS__FK_CHECKER_STEP);

        return _checkerStep;
    }

    @Override
    public SaveResultStepEmailCcs as(String alias) {
        return new SaveResultStepEmailCcs(DSL.name(alias), this);
    }

    @Override
    public SaveResultStepEmailCcs as(Name alias) {
        return new SaveResultStepEmailCcs(alias, this);
    }

    @Override
    public SaveResultStepEmailCcs as(Table<?> alias) {
        return new SaveResultStepEmailCcs(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public SaveResultStepEmailCcs rename(String name) {
        return new SaveResultStepEmailCcs(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public SaveResultStepEmailCcs rename(Name name) {
        return new SaveResultStepEmailCcs(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public SaveResultStepEmailCcs rename(Table<?> name) {
        return new SaveResultStepEmailCcs(name.getQualifiedName(), null);
    }
}