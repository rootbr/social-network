package com.rootbr.network.application.command;

import static com.rootbr.network.adapter.out.db.MessagePortImpl.ROLE_ASSISTANT;

import com.rootbr.network.application.SocialNetworkApplication;
import com.rootbr.network.application.visitor.ChatsVisitor;
import java.sql.Connection;

public class CreateChatCommand extends TransactionalCommand {

  private final SocialNetworkApplication application;
  private final String chatId;
  private final ChatsVisitor response;
  private final String titleChat;
  private final String aiResponse;

  public CreateChatCommand(final SocialNetworkApplication application, final String chatId, final ChatsVisitor response) {
    this.application = application;
    this.chatId = chatId;
    this.response = response;
    this.titleChat = application.messageProperties.getProperty("title");
    this.aiResponse = application.messageProperties.getProperty("ai1");
  }

  @Override
  protected void transaction(final Connection connection) throws Exception {
    application.chatPort.createChat(connection, chatId, principalId, "Договор аренды жилья", application.questions.firstEntry().getKey(), instant);
    application.messagePort.createMessage(connection, chatId, 0, ROLE_ASSISTANT, aiResponse, instant);
  }

  @Override
  protected void afterTransaction() throws Exception {
    response.visitChat(chatId, titleChat, aiResponse, instant.toString());
  }
}