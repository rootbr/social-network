package com.rootbr.network.application.visitor;

public interface UsersVisitor {

  void visitUser(final String id, final String firstName, final String lastName, 
                final String birthdate, final String biography, final String city);
}
