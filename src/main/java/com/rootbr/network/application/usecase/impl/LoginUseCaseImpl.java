package com.rootbr.network.application.usecase.impl;

import static com.rootbr.network.application.SocialNetworkApplication.ANONYMOUS;

import com.rootbr.network.application.usecase.LoginUseCase;
import com.rootbr.network.domain.AllUsers;
import com.rootbr.network.domain.engine.Command;
import com.rootbr.network.domain.engine.Invoker;
import com.rootbr.network.domain.port.rest.model.LoginPostRequestRestDto;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginUseCaseImpl implements LoginUseCase {
  private final AllUsers allUsers;
  private final PasswordEncoder passwordEncoder;
  private final Invoker invoker;

  public LoginUseCaseImpl(final AllUsers allUsers, final PasswordEncoder passwordEncoder, final Invoker invoker) {
    this.allUsers = allUsers;
    this.passwordEncoder = passwordEncoder;
    this.invoker = invoker;
  }

  @Override
  public boolean login(final LoginPostRequestRestDto request) {
    final LoginCommand command = new LoginCommand(request);
    invoker.invoke(command, ANONYMOUS);
    return command.result;
  }

  private class LoginCommand extends Command {
    private final LoginPostRequestRestDto request;
    private boolean result = false;

    public LoginCommand(final LoginPostRequestRestDto request) {
      this.request = request;
    }

    @Override
    protected void doExecute() {
      result = allUsers.getById(request.getId()).login(passwordEncoder, request.getPassword());
    }
  }
}
