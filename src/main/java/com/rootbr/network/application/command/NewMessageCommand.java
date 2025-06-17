package com.rootbr.network.application.command;

import static com.rootbr.network.adapter.out.db.MessagePortImpl.ROLE_ASSISTANT;
import static com.rootbr.network.adapter.out.db.MessagePortImpl.ROLE_USER;

import com.rootbr.network.adapter.in.rest.ApplicationConfiguration.Question;
import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.application.visitor.AnswerVisitor;
import java.sql.Connection;
import java.util.Map;

public class NewMessageCommand extends TransactionalCommand {

  private final String chatId;
  private final int messageId;
  private final Question question;
  private final String userAnswer;
  private final AnswerVisitor response;
  private String aiResponse;
  private String answerValue;
  private Question nextQuestion;

  public NewMessageCommand(final SocialNetworkApplication application, final String chatId,
      final int messageId, final String questionType, final String userAnswer,
      final AnswerVisitor response) {
    this.chatId = chatId;
    this.messageId = messageId;
    this.question = application.questions.get(questionType);
    this.userAnswer = userAnswer;
    this.response = response;
  }

  @Override
  protected void beforeTransaction() throws Exception {
    // Получаем предыдущие ответы из базы перед вызовом AI
    final Map<String, String> previousAnswers;
    try (final Connection connection = application.dataSource.getConnection()) {
      previousAnswers = application.parametersPort.getParametersByChatId(connection, chatId);
    }
    application.aiPort.chat(question, userAnswer, previousAnswers, this::handler);
  }

  private void handler(final String answerValue, final String aiMessage, final Question nextQuestion) {
    this.nextQuestion = nextQuestion;
    this.answerValue = answerValue;
    this.aiResponse = aiMessage;
    if (nextQuestion == null) {
      aiResponse =
          "Отлично! Я собрал всю необходимую информацию для составления договора аренды. " +
              "Теперь вы можете скачать готовый документ, нажав на кнопку 'Скачать договор'.";
    }
  }

  @Override
  protected void transaction(final Connection connection) throws Exception {
    application.messagePort.createMessage(connection, chatId, messageId, ROLE_USER, userAnswer, instant);
    application.messagePort.createMessage(connection, chatId, messageId + 1, ROLE_ASSISTANT, aiResponse, instant);
    application.chatPort.updateChat(connection, chatId, nextQuestion != null ? nextQuestion.key() : null, instant, aiResponse);
    if (answerValue != null) {
      application.parametersPort.create(connection, chatId, question.key(), answerValue);
    }
  }

  @Override
  protected void afterTransaction() throws Exception {
    response.visitAnswer(messageId + 1, aiResponse, nextQuestion);
  }
}