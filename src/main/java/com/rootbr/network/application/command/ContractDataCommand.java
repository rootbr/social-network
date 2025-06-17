package com.rootbr.network.application.command;

import com.rootbr.network.application.SocialNetworkApplication;
import java.sql.Connection;
import java.util.Map;
import java.util.function.BiConsumer;

public class ContractDataCommand extends TransactionalCommand {

  private final SocialNetworkApplication application;
  private final String chatId;
  private final BiConsumer<String, String> response;
  private Map<String, String> parameters;

  public ContractDataCommand(final SocialNetworkApplication application, final String chatId, final BiConsumer<String, String> response) {
    this.application = application;
    this.chatId = chatId;
    this.response = response;
  }

  @Override
  protected void transaction(final Connection connection) throws Exception {
    parameters = application.parametersPort.getParametersByChatId(connection, chatId);
  }

  @Override
  protected void afterTransaction() {
    for (final Map.Entry<String, String> entry : parameters.entrySet()) {
      response.accept(entry.getKey(), entry.getValue());
    }
  }
}