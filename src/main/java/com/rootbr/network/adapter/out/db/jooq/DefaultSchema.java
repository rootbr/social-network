/*
 * This file is generated by jOOQ.
 */
package com.rootbr.network.adapter.out.db.jooq;


import com.rootbr.network.adapter.out.db.jooq.tables.Principals;
import com.rootbr.network.adapter.out.db.jooq.tables.Users;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * The table <code>principals</code>.
     */
    public final Principals PRINCIPALS = Principals.PRINCIPALS;

    /**
     * The table <code>users</code>.
     */
    public final Users USERS = Users.USERS;

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super(DSL.name(""), null, DSL.comment(""));
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.asList(
            Principals.PRINCIPALS,
            Users.USERS
        );
    }
}
