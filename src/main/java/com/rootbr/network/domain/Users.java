package com.rootbr.network.domain;

import com.rootbr.network.domain.port.rest.model.UserRestDto;
import java.util.ArrayList;
import java.util.List;

public class Users {
  private final List<User> userList;

  public Users(final List<User> userList) {
    this.userList = new ArrayList<>(userList);
  }

  public void write(final List<UserRestDto> response) {
    for (final User user : userList) {
      final UserRestDto.Builder responseBuilder = UserRestDto.builder();
      user.write(responseBuilder);
      response.add(responseBuilder.build());
    }
  }
}
