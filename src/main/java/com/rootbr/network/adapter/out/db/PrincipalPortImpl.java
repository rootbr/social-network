package com.rootbr.network.adapter.out.db;

import static com.rootbr.network.adapter.out.db.jooq.Tables.PRINCIPALS;
import static com.rootbr.network.adapter.out.db.jooq.Tables.USERS;

import com.rootbr.network.adapter.out.db.jooq.tables.records.UsersRecord;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.application.port.PrincipalPort;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class PrincipalPortImpl implements PrincipalPort {

  private final DSLContext dsl;

  public PrincipalPortImpl(final DataSource dataSource) {
    this.dsl = DSL.using(dataSource, SQLDialect.POSTGRES);
  }

  @Override
  public void save(final String principalId, final String encodedPassword) {
//    dsl.insertInto(DSL.table("principals"))
//        .set(DSL.field("id"), principalId)
//        .set(DSL.field("encoded_password"), encodedPassword)
//        .execute();

    dsl.insertInto(PRINCIPALS)
          .set(PRINCIPALS.ID, principalId)
          .set(PRINCIPALS.ENCODED_PASSWORD, encodedPassword)
          .execute();
  }

  @Override
  public String getEncodedPassword(final String principalId) {
    return dsl.selectFrom(PRINCIPALS)
        .where(PRINCIPALS.ID.eq(principalId))
        .fetchOne(PRINCIPALS.ENCODED_PASSWORD);
  }
}
