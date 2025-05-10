package com.rootbr.network.domain.engine;

import java.io.IOException;
import java.time.Instant;

public abstract class Command {

  public final void execute() throws IOException {
    doExecute();
  }

  public abstract void doExecute() throws IOException;
}
