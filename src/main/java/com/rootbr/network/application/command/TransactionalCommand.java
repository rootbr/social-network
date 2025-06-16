package com.rootbr.network.application.command;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public abstract class TransactionalCommand extends Command {

  @Override
  public final void execute(final DataSource dataSource) {
    try {
      beforeTransaction();
      try (final Connection connection = dataSource.getConnection()) {
        try {
          connection.setAutoCommit(false);
          transaction(connection);
          connection.commit();
          afterTransaction();
        } catch (final Exception e) {
          try {
            connection.rollback();
            rollback(connection);
          } catch (final SQLException rollbackEx) {
            e.addSuppressed(rollbackEx);
            log.error("Failed to rollback transaction", rollbackEx);
          }
          throw e;
        }
      }
    } catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  protected void beforeTransaction() throws Exception {
    // Default implementation does nothing, can be overridden if needed
  }

  protected abstract void transaction(final Connection connection) throws Exception;

  protected void afterTransaction() throws Exception {
    // Default implementation does nothing, can be overridden if needed
  }

  protected void rollback(final Connection connection) throws Exception {
    // Default implementation does nothing, can be overridden if needed
  }
}
