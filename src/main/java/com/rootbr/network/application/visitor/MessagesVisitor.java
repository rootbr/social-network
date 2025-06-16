package com.rootbr.network.application.visitor;

public interface MessagesVisitor {

  void visitMessage(int messageId, String role, String content, String timestamp, boolean isFirst, boolean isLast) throws Exception;

}
