package com.rootbr.network.application.visitor;

public interface ChatsVisitor {

  void visitChat(final String id, final String title, final String lastMessage, final String date) throws Exception;
}
