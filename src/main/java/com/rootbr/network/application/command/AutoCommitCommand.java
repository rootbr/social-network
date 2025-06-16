package com.rootbr.network.application.command;

import java.sql.Connection;
import javax.sql.DataSource;

public abstract class AutoCommitCommand extends Command {

  @Override
  public final void execute(final DataSource dataSource) {
    try (final Connection connection = dataSource.getConnection()) {
      doCommand(connection);
    } catch (final Exception e) {
      throw new RuntimeException("Database connection failed", e);
    }
  }

  protected abstract void doCommand(final Connection connection) throws Exception;
}
