package com.rootbr.network.repository.impl;

import com.rootbr.network.model.User;
import com.rootbr.network.repository.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jooq.impl.DSL.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
  private final DSLContext dsl;

  @Autowired
  public UserRepositoryImpl(DSLContext dsl) {
    this.dsl = dsl;
  }

  @Override
  @Transactional
  public String createUser(User user, String password) {
    // Simple password hashing, in a real application use a proper password hashing library
    String passwordHash = String.valueOf(password.hashCode());

    dsl.insertInto(table("users"))
        .set(field("id"), user.getId())
        .set(field("first_name"), user.getFirstName())
        .set(field("second_name"), user.getSecondName())
        .set(field("birthdate"), user.getBirthdate())
        .set(field("biography"), user.getBiography())
        .set(field("city"), user.getCity())
        .set(field("password_hash"), passwordHash)
        .execute();

    return user.getId();
  }

  @Override
  public Optional<User> getUserById(String id) {
    Record record = dsl.select()
        .from(table("users"))
        .where(field("id").eq(id))
        .fetchOne();

    if (record == null) {
      return Optional.empty();
    }

    return Optional.of(mapRecordToUser(record));
  }

  @Override
  public List<User> searchUsers(String firstName, String lastName) {
    Result<Record> result = dsl.select()
        .from(table("users"))
        .where(field("first_name").like(firstName + "%")
            .and(field("second_name").like(lastName + "%")))
        .fetch();

    List<User> users = new ArrayList<>();
    for (Record record : result) {
      users.add(mapRecordToUser(record));
    }

    return users;
  }

  @Override
  public boolean authenticateUser(String id, String password) {
    // Simple password hashing, in a real application use a proper password hashing library
    String passwordHash = String.valueOf(password.hashCode());

    int count = dsl.selectCount()
        .from(table("users"))
        .where(field("id").eq(id)
            .and(field("password_hash").eq(passwordHash)))
        .fetchOne(0, int.class);

    return count > 0;
  }

  @Override
  @Transactional
  public void addFriend(String userId, String friendId) {
    dsl.insertInto(table("friends"))
        .set(field("user_id"), userId)
        .set(field("friend_id"), friendId)
        .onDuplicateKeyIgnore()
        .execute();
  }

  @Override
  @Transactional
  public void removeFriend(String userId, String friendId) {
    dsl.deleteFrom(table("friends"))
        .where(field("user_id").eq(userId)
            .and(field("friend_id").eq(friendId)))
        .execute();
  }

  private User mapRecordToUser(Record record) {
    User user = new User();
    user.setId(record.get("id", String.class));
    user.setFirstName(record.get("first_name", String.class));
    user.setSecondName(record.get("second_name", String.class));
    user.setBirthdate(record.get("birthdate", LocalDate.class));
    user.setBiography(record.get("biography", String.class));
    user.setCity(record.get("city", String.class));
    return user;
  }
}
