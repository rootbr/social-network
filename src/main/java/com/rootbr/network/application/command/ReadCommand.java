package com.rootbr.network.application.command;

import java.sql.Connection;
import javax.sql.DataSource;

public abstract class ReadCommand extends Command {

  @Override
  public final void execute(final DataSource dataSource) {
    try (final Connection connection = dataSource.getConnection()) {
      connection.setReadOnly(true);
      doCommand(connection);
      afterCommand();
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected abstract void doCommand(final Connection connection) throws Exception;
  protected void afterCommand() throws Exception { };
}
