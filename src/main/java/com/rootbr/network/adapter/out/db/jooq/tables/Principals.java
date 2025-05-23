/*
 * This file is generated by jOOQ.
 */
package com.rootbr.network.adapter.out.db.jooq.tables;


import com.rootbr.network.adapter.out.db.jooq.DefaultSchema;
import com.rootbr.network.adapter.out.db.jooq.Keys;
import com.rootbr.network.adapter.out.db.jooq.tables.records.PrincipalsRecord;

import java.util.Collection;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.PlainSQL;
import org.jooq.QueryPart;
import org.jooq.SQL;
import org.jooq.Schema;
import org.jooq.Select;
import org.jooq.Stringly;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Principals extends TableImpl<PrincipalsRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>principals</code>
     */
    public static final Principals PRINCIPALS = new Principals();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PrincipalsRecord> getRecordType() {
        return PrincipalsRecord.class;
    }

    /**
     * The column <code>principals.id</code>.
     */
    public final TableField<PrincipalsRecord, String> ID = createField(DSL.name("id"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>principals.encoded_password</code>.
     */
    public final TableField<PrincipalsRecord, String> ENCODED_PASSWORD = createField(DSL.name("encoded_password"), SQLDataType.VARCHAR(255).nullable(false), this, "");

    private Principals(Name alias, Table<PrincipalsRecord> aliased) {
        this(alias, aliased, (Field<?>[]) null, null);
    }

    private Principals(Name alias, Table<PrincipalsRecord> aliased, Field<?>[] parameters, Condition where) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table(), where);
    }

    /**
     * Create an aliased <code>principals</code> table reference
     */
    public Principals(String alias) {
        this(DSL.name(alias), PRINCIPALS);
    }

    /**
     * Create an aliased <code>principals</code> table reference
     */
    public Principals(Name alias) {
        this(alias, PRINCIPALS);
    }

    /**
     * Create a <code>principals</code> table reference
     */
    public Principals() {
        this(DSL.name("principals"), null);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<PrincipalsRecord> getPrimaryKey() {
        return Keys.PK_PRINCIPALS;
    }

    @Override
    public Principals as(String alias) {
        return new Principals(DSL.name(alias), this);
    }

    @Override
    public Principals as(Name alias) {
        return new Principals(alias, this);
    }

    @Override
    public Principals as(Table<?> alias) {
        return new Principals(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public Principals rename(String name) {
        return new Principals(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Principals rename(Name name) {
        return new Principals(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public Principals rename(Table<?> name) {
        return new Principals(name.getQualifiedName(), null);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Principals where(Condition condition) {
        return new Principals(getQualifiedName(), aliased() ? this : null, null, condition);
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Principals where(Collection<? extends Condition> conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Principals where(Condition... conditions) {
        return where(DSL.and(conditions));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Principals where(Field<Boolean> condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Principals where(SQL condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Principals where(@Stringly.SQL String condition) {
        return where(DSL.condition(condition));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Principals where(@Stringly.SQL String condition, Object... binds) {
        return where(DSL.condition(condition, binds));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    @PlainSQL
    public Principals where(@Stringly.SQL String condition, QueryPart... parts) {
        return where(DSL.condition(condition, parts));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Principals whereExists(Select<?> select) {
        return where(DSL.exists(select));
    }

    /**
     * Create an inline derived table from this table
     */
    @Override
    public Principals whereNotExists(Select<?> select) {
        return where(DSL.notExists(select));
    }
}
