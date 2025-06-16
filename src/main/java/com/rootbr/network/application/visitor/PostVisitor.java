package com.rootbr.network.application.visitor;

public interface PostVisitor {
  void visitPost(String id, String text, String authorUserId);
}