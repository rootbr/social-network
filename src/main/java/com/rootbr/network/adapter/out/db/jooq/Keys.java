/*
 * This file is generated by jOOQ.
 */
package com.rootbr.network.adapter.out.db.jooq;


import com.rootbr.network.adapter.out.db.jooq.tables.Principals;
import com.rootbr.network.adapter.out.db.jooq.tables.Users;
import com.rootbr.network.adapter.out.db.jooq.tables.records.PrincipalsRecord;
import com.rootbr.network.adapter.out.db.jooq.tables.records.UsersRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in the
 * default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<PrincipalsRecord> PK_PRINCIPALS = Internal.createUniqueKey(Principals.PRINCIPALS, DSL.name("PK_PRINCIPALS"), new TableField[] { Principals.PRINCIPALS.ID }, true);
    public static final UniqueKey<UsersRecord> PK_USERS = Internal.createUniqueKey(Users.USERS, DSL.name("PK_USERS"), new TableField[] { Users.USERS.ID }, true);
}
