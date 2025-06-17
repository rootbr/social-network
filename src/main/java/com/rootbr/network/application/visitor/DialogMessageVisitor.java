package com.rootbr.network.application.visitor;

import java.io.IOException;

public interface DialogMessageVisitor {
  void visitMessage(String from, String to, String text) throws IOException;
}