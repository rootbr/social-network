package com.rootbr.network.domain.engine;

import java.io.IOException;

public class Invoker {

  public void invoke(final CommandAuthor commandAuthor, final Command command) throws IOException {
    command.setSource(commandAuthor);
    command.doExecute();
  }
}
