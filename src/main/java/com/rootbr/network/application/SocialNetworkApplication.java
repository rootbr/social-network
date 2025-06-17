package com.rootbr.network.application;

import com.fasterxml.jackson.core.JsonFactory;
import com.rootbr.network.adapter.in.rest.TokenService;
import com.rootbr.network.application.command.Command;
import com.rootbr.network.application.command.ReadCommand;
import com.rootbr.network.application.command.TransactionalCommand;
import com.rootbr.network.application.port.PasswordEncoder;
import com.rootbr.network.application.port.PrincipalPort;
import com.rootbr.network.application.port.UserPort;
import com.rootbr.network.application.visitor.DialogMessageVisitor;
import com.rootbr.network.application.visitor.PostVisitor;
import com.rootbr.network.application.visitor.UsersVisitor;
import java.sql.Connection;
import javax.sql.DataSource;

public class SocialNetworkApplication {

  public final JsonFactory factory;
  public final Principal anonymous;
  public final DataSource dataSource;
  public final UserPort userPort;
  public final PrincipalPort principalPort;
  public final PasswordEncoder passwordEncoder;

  public SocialNetworkApplication(
      final JsonFactory factory,
      final DataSource dataSource,
      final PasswordEncoder passwordEncoder,
      final UserPort userPort,
      final PrincipalPort principalPort
  ) {
    this.factory = factory;
    this.dataSource = dataSource;
    this.userPort = userPort;
    this.principalPort = principalPort;
    this.passwordEncoder = passwordEncoder;
    anonymous = new Principal(dataSource, "anonymous", "anonymous", "") {
      @Override
      public String generateToken(final TokenService tokenService) {
        return "anonymous-token";
      }

      @Override
      public boolean verifyPassword(final PasswordEncoder passwordEncoder,
          final String rawPassword) {
        return false;
      }
    };
  }

  public Command registerUserCommand(final String userId, final String firstName, final String secondName,
      final String birthdate, final String biography, final String city, final String password) {
    return new TransactionalCommand() {
      @Override
      protected void transaction(final Connection connection) throws Exception {
        principalPort.create(connection, userId, userId, passwordEncoder.encode(password));
        userPort.create(connection, userId, firstName, secondName, birthdate, biography, city);
      }
    };
  }

  public Command changePasswordCommand(final String currentPassword, final String newPassword) {
    return new TransactionalCommand() {
      @Override
      protected void transaction(final Connection connection) throws Exception {
        final Principal principal = new Principal(dataSource);
        principalPort.readEncodedPasswordByPrincipalId(connection, principalId,
            principal.visitor());
        if (principal.verifyPassword(passwordEncoder, currentPassword)) {
          principalPort.changePassword(connection, principalId,
              passwordEncoder.encode(newPassword));
        }
      }
    };
  }

  public Command getUserByIdCommand(final String userId, final UsersVisitor visitor) {
    return new ReadCommand() {
      @Override
      protected void doCommand(final Connection connection) throws Exception {
        userPort.getUserById(connection, userId, visitor);
      }
    };
  }

  public Command searchUsersCommand(final String firstName, final String lastName, final UsersVisitor visitor) {
    return new ReadCommand() {
      @Override
      protected void doCommand(final Connection connection) throws Exception {
        userPort.searchUsers(connection, firstName, lastName, visitor);
      }
    };
  }

  // Post commands
  public Command createPostCommand(final String postId, final String text) {
    return new TransactionalCommand() {
      @Override
      protected void transaction(final Connection connection) throws Exception {
        // TODO: Implement post creation
      }
    };
  }

  public Command updatePostCommand(final String postId, final String text) {
    return new TransactionalCommand() {
      @Override
      protected void transaction(final Connection connection) throws Exception {
        // TODO: Implement post update
      }
    };
  }

  public Command deletePostCommand(final String postId) {
    return new TransactionalCommand() {
      @Override
      protected void transaction(final Connection connection) throws Exception {
        // TODO: Implement post deletion
      }
    };
  }

  public Command getPostByIdCommand(final String postId, final PostVisitor visitor) {
    return new ReadCommand() {
      @Override
      protected void doCommand(final Connection connection) throws Exception {
        // TODO: Implement get post by id
      }
    };
  }

  public Command getUserFeedCommand(final int offset, final int limit, final PostVisitor visitor) {
    return new ReadCommand() {
      @Override
      protected void doCommand(final Connection connection) throws Exception {
        // TODO: Implement user feed
      }
    };
  }

  // Friend commands
  public Command addFriendCommand(final String friendUserId) {
    return new TransactionalCommand() {
      @Override
      protected void transaction(final Connection connection) throws Exception {
        // TODO: Implement add friend
      }
    };
  }

  public Command removeFriendCommand(final String friendUserId) {
    return new TransactionalCommand() {
      @Override
      protected void transaction(final Connection connection) throws Exception {
        // TODO: Implement remove friend
      }
    };
  }

  // Dialog commands
  public Command sendMessageCommand(final String toUserId, final String text) {
    return new TransactionalCommand() {
      @Override
      protected void transaction(final Connection connection) throws Exception {
        // TODO: Implement send message
      }
    };
  }

  public Command getDialogMessagesCommand(final String otherUserId, final DialogMessageVisitor visitor) {
    return new ReadCommand() {
      @Override
      protected void doCommand(final Connection connection) throws Exception {
        // TODO: Implement get dialog messages
      }
    };
  }

  public Principal anonymous() {
    return anonymous;
  }

  public Principal principal(final String userId, final String email) {
    return new Principal(dataSource, userId, email, null);
  }

  public Principal login(final String login, final String password) {
    final Principal principal = new Principal(dataSource);
    anonymous.execute(new ReadCommand() {
      @Override
      protected void doCommand(final Connection connection) throws Exception {
        principalPort.findPrincipalByLogin(connection, login, principal.visitor());
      }
    });
    if (principal.verifyPassword(passwordEncoder, password)) {
      return principal;
    } else {
      return null;
    }
  }
}