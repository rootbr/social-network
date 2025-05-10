package com.rootbr.network.application.usecase.impl;

import com.rootbr.network.application.usecase.GetUserByIdUseCase;
import com.rootbr.network.domain.AllUsers;
import com.rootbr.network.domain.engine.Command;
import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.engine.Invoker;
import com.rootbr.network.domain.port.rest.model.UserRestDto;
import java.io.IOException;

public class GetUserByIdUseCaseImpl implements GetUserByIdUseCase {

  private final AllUsers allUsers;
  private final Invoker invoker;

  public GetUserByIdUseCaseImpl(final AllUsers allUsers, final Invoker invoker) {
    this.allUsers = allUsers;
    this.invoker = invoker;
  }

  @Override
  public void getUserById(final CommandAuthor commandAuthor, final String id,
      final UserRestDto.Builder response)
      throws IOException {
    invoker.invoke(
        commandAuthor, new Command() {
          @Override
          protected void doExecute() {
            allUsers.getById(id).write(response);
          }
        }
    );
  }
}
