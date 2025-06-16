package com.rootbr.network.application.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggingPipedOutputStream extends PipedOutputStream {
  private static final Logger log = LoggerFactory.getLogger(LoggingPipedOutputStream.class);
  private final ByteArrayOutputStream buffer;

  public LoggingPipedOutputStream(final PipedInputStream snk) throws IOException {
    super(snk);
    if (log.isDebugEnabled()) {
      buffer = new ByteArrayOutputStream();
    } else {
      buffer = null; // Avoid unnecessary memory allocation when debug logging is disabled
    }
  }

  @Override
  public void write(final int b) throws IOException {
    if (log.isDebugEnabled()) {
      buffer.write(b);
    }
    super.write(b);
  }

  @Override
  public void write(final byte[] b, final int off, final int len) throws IOException {
    if (log.isDebugEnabled()) {
      buffer.write(b, off, len);
    }
    super.write(b, off, len);
  }

  @Override
  public void close() throws IOException {
    if (log.isDebugEnabled()) {
      log.debug("Data written to PipedOutputStream: {}", buffer.toString(StandardCharsets.UTF_8));
    }
    super.close();
  }
}
