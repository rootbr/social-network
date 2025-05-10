package com.rootbr.network.domain.engine;

import java.io.IOException;
import java.time.Instant;

public abstract class Command {

  protected CommandAuthor commandAuthor;
  protected Instant timestamp;

  void setSource(final CommandAuthor commandAuthor) {
    this.commandAuthor = commandAuthor;
  }

  public final void execute() throws IOException {
    timestamp = Instant.now();
    doExecute();
  }

  protected abstract void doExecute() throws IOException;
}
