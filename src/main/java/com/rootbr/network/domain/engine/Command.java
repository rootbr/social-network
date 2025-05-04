package com.rootbr.network.domain.engine;

import java.time.Instant;

public abstract class Command {

  protected CommandAuthor commandAuthor;
  protected Instant timestamp;

  void setSource(final CommandAuthor commandAuthor) {
    this.commandAuthor = commandAuthor;
  }

  public final void execute() {
    timestamp = Instant.now();
    doExecute();
  }

  protected abstract void doExecute();
}
