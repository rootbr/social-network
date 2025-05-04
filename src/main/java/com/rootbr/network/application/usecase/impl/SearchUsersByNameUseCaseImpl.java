package com.rootbr.network.application.usecase.impl;

import com.rootbr.network.application.usecase.SearchUsersByNameUseCase;
import com.rootbr.network.domain.AllUsers;
import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.engine.Command;
import com.rootbr.network.domain.engine.Invoker;
import com.rootbr.network.domain.port.rest.model.UserRestDto;
import java.util.List;

public class SearchUsersByNameUseCaseImpl implements SearchUsersByNameUseCase {

  private final AllUsers allUsers;
  private final Invoker invoker;

  public SearchUsersByNameUseCaseImpl(final AllUsers allUsers, final Invoker invoker) {
    this.allUsers = allUsers;
    this.invoker = invoker;
  }

  @Override
  public void searchUsers(final CommandAuthor commandAuthor, String firstName, String lastName, List<UserRestDto> response) {
    invoker.invoke(
        new Command() {
          @Override
          protected void doExecute() {
            allUsers.search(firstName, lastName).write(response);
          }
        },
        commandAuthor
    );
  }
}
