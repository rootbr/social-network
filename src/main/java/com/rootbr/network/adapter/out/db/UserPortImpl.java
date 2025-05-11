package com.rootbr.network.adapter.out.db;

import static com.rootbr.network.adapter.out.db.jooq.Tables.USERS;

import com.rootbr.network.adapter.out.db.jooq.tables.records.UsersRecord;
import com.rootbr.network.domain.User;
import com.rootbr.network.domain.Users;
import com.rootbr.network.domain.port.db.UserPort;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

public class UserPortImpl implements UserPort {

  private final DSLContext dsl;

  public UserPortImpl(final DataSource dataSource) {
    this.dsl = DSL.using(dataSource, SQLDialect.POSTGRES);
  }

  @Override
  public User getUserById(final String id) {
    final UsersRecord record = dsl.selectFrom(USERS)
        .where(USERS.ID.eq(id))
        .fetchOne();
    return record == null ? null : new User(record.getId(), record.getFirstName(),
        record.getLastName(), record.getCity(), record.getBirthDate(), record.getBiography());
  }

  @Override
  public Users searchUsers(final String firstName, final String lastName) {
    final Result<UsersRecord> records = dsl.selectFrom(USERS)
        .where(
            USERS.FIRST_NAME.containsIgnoreCase(firstName)
                .and(USERS.LAST_NAME.containsIgnoreCase(lastName))
        )
        .fetch();

    final List<User> userList = new ArrayList<>();
    for (final UsersRecord record : records) {
      userList.add(
          new User(
              record.getId(),
              record.getFirstName(),
              record.getLastName(),
              record.getCity(),
              record.getBirthDate(),
              record.getBiography()
          )
      );
    }

    return new Users(userList);
  }

  @Override
  public void createUser(final String id, final String firstName, final String secondName,
      final String city, final LocalDate birthdate, final String biography) {
    dsl.insertInto(USERS)
        .set(USERS.ID, id)
        .set(USERS.FIRST_NAME, firstName)
        .set(USERS.LAST_NAME, secondName)
        .set(USERS.CITY, city)
        .set(USERS.BIRTH_DATE, birthdate)
        .set(USERS.BIOGRAPHY, biography)
        .execute();
  }
}