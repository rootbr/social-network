package com.rootbr.network.application;

import com.rootbr.network.adapter.in.rest.TokenService;
import com.rootbr.network.application.command.Command;
import com.rootbr.network.application.port.PasswordEncoder;
import com.rootbr.network.application.visitor.PrincipalVisitor;
import javax.sql.DataSource;

public class Principal {

  private final DataSource dataSource;
  private String id;
  private String login;
  private String encodedPassword;

  Principal(final DataSource dataSource, final String id, final String login, final String encodedPassword) {
    this.dataSource = dataSource;
    this.id = id;
    this.login = login;
    this.encodedPassword = encodedPassword;
  }

  Principal(final DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void execute(final Command... commands) {
    for (final Command command : commands) {
      if (command.authorize(id, login)) {
        command.execute(dataSource);
      }
    }
  }

  public String generateToken(final TokenService tokenService) {
    return tokenService.generateToken(id, login);
  }

  public boolean verifyPassword(final PasswordEncoder passwordEncoder, final String rawPassword) {
    if (encodedPassword == null) {
      return false;
    }
    return passwordEncoder.matches(rawPassword, encodedPassword);
  }

  public PrincipalVisitor visitor() {
    return new InternalPrincipalVisitor();
  }

  private class InternalPrincipalVisitor implements PrincipalVisitor {

    @Override
    public void visitId(final String id) {
      Principal.this.id = id;
    }

    @Override
    public void visitEncodedPassword(final String encodedPassword) {
      Principal.this.encodedPassword = encodedPassword;
    }

    @Override
    public void visitLogin(final String login) {
      Principal.this.login = login;
    }
  }
}
