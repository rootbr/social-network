package com.rootbr.network.application.usecase.impl;

import com.rootbr.network.application.usecase.RegisterUserUseCase;
import com.rootbr.network.domain.AllUsers;
import com.rootbr.network.domain.engine.Command;
import com.rootbr.network.domain.engine.Invoker;
import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.port.rest.model.UserRegisterPost200ResponseRestDto;
import com.rootbr.network.domain.port.rest.model.UserRegisterPost200ResponseRestDto.Builder;
import com.rootbr.network.domain.port.rest.model.UserRegisterPostRequestRestDto;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

  private final AllUsers allUsers;
  private final PasswordEncoder passwordEncoder;
  private final Invoker invoker;

  public RegisterUserUseCaseImpl(final AllUsers allUsers, final PasswordEncoder passwordEncoder, final Invoker invoker) {
    this.allUsers = allUsers;
    this.passwordEncoder = passwordEncoder;
    this.invoker = invoker;
  }

  @Override
  public void registerUser(final CommandAuthor commandAuthor, final UserRegisterPostRequestRestDto request, final Builder response) {
    invoker.invoke(new RegisterUserCommand(
            UUID.randomUUID().toString(),
            request.getFirstName(),
            request.getSecondName(),
            request.getCity(),
            request.getBirthdate(),
            request.getBiography(),
            passwordEncoder.encode(request.getPassword()),
            response
        ),
        commandAuthor
    );
  }

  private class RegisterUserCommand extends Command {

    private final String id;
    private final String firstName;
    private final String secondName;
    private final String city;
    private final LocalDate birthdate;
    private final String biography;
    private final String encodedPassword;
    private final UserRegisterPost200ResponseRestDto.Builder response;

    public RegisterUserCommand(
        final String id,
        final String firstName,
        final String secondName,
        final String city,
        final LocalDate birthdate,
        final String biography,
        final String encodedPassword,
        final UserRegisterPost200ResponseRestDto.Builder response
    ) {
      this.id = id;
      this.firstName = firstName;
      this.secondName = secondName;
      this.city = city;
      this.birthdate = birthdate;
      this.biography = biography;
      this.encodedPassword = encodedPassword;
      this.response = response;
    }

    @Override
    public void doExecute() {
      allUsers.registerNewUser(id, firstName, secondName, city, birthdate, biography, encodedPassword)
          .write(response);
    }
  }
}
