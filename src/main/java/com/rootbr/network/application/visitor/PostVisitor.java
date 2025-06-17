package com.rootbr.network.application.visitor;

import java.io.IOException;

public interface PostVisitor {
  void visitPost(String id, String text, String authorUserId) throws IOException;
}