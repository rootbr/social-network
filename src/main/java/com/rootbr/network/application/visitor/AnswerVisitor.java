package com.rootbr.network.application.visitor;

import com.rootbr.network.adapter.in.rest.ApplicationConfiguration.Question;

public interface AnswerVisitor {
  void visitAnswer(int messageId, String content, Question nextQuestion) throws Exception;
}
