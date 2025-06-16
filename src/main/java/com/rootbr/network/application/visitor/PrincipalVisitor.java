package com.rootbr.network.application.visitor;

public interface PrincipalVisitor {

  default void visitId(final String id) {}

  default void visitEncodedPassword(final String encodedPassword) {}

  default void visitLogin(final String login) {}
}
