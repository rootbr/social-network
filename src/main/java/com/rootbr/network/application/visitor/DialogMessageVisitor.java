package com.rootbr.network.application.visitor;

public interface DialogMessageVisitor {
  void visitMessage(String from, String to, String text);
}