package com.rootbr.network.domain.engine;

public class Invoker {

  public void invoke(final Command command, final CommandAuthor commandAuthor) {
    command.setSource(commandAuthor);
    command.doExecute();
  }
}
