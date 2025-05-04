package com.rootbr.network.adapter.out.db;

import static com.rootbr.network.adapter.out.db.jooq.Tables.USERS;

import com.rootbr.network.adapter.out.db.jooq.tables.records.UsersRecord;
import com.rootbr.network.domain.User;
import com.rootbr.network.domain.Users;
import com.rootbr.network.domain.port.db.UserPort;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Result;

public class UserPortImpl implements UserPort {

  private final DSLContext dsl;

  public UserPortImpl(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  public User getUserById(final String id) {
    UsersRecord record = dsl.selectFrom(USERS)
        .where(USERS.ID.eq(id))
        .fetchOne();

    if (record == null) {
      return null;
    }

    return new User(
        record.getId(),
        record.getFirstName(),
        record.getLastName(),
        record.getCity(),
        record.getBirthDate(),
        record.getBiography(),
        record.getEncodedPassword()
    );
  }

  @Override
  public Users searchUsers(final String firstName, final String lastName) {
    Result<UsersRecord> records = dsl.selectFrom(USERS)
        .where(
            USERS.FIRST_NAME.containsIgnoreCase(firstName)
                .and(USERS.LAST_NAME.containsIgnoreCase(lastName))
        )
        .fetch();

    List<User> userList = new ArrayList<>();
    for (UsersRecord record : records) {
      userList.add(new User(
          record.getId(),
          record.getFirstName(),
          record.getLastName(),
          record.getCity(),
          record.getBirthDate(),
          record.getBiography(),
          record.getEncodedPassword()
      ));
    }

    return new Users(userList);
  }

  @Override
  public void createUser(final String id, final String firstName, final String secondName,
      final String city, final LocalDate birthdate, final String biography,
      final String encodedPassword) {
    dsl.insertInto(USERS)
        .set(USERS.ID, id)
        .set(USERS.FIRST_NAME, firstName)
        .set(USERS.LAST_NAME, secondName)
        .set(USERS.CITY, city)
        .set(USERS.BIRTH_DATE, birthdate)
        .set(USERS.BIOGRAPHY, biography)
        .set(USERS.ENCODED_PASSWORD, encodedPassword)
        .execute();
  }
}