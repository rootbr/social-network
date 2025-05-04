package com.rootbr.network.adapter.in.rest.exception;

import java.util.Objects;

public class ErrorResponse {
  private String message;
  private String requestId;
  private int code;

  public ErrorResponse(final String message, final String requestId, final int code) {
    this.message = message;
    this.requestId = requestId;
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(final String requestId) {
    this.requestId = requestId;
  }

  public int getCode() {
    return code;
  }

  public void setCode(final int code) {
    this.code = code;
  }

  @Override
  public final boolean equals(final Object o) {
    if (!(o instanceof final ErrorResponse that)) {
      return false;
    }

    return code == that.code && Objects.equals(message, that.message)
        && Objects.equals(requestId, that.requestId);
  }

  @Override
  public int hashCode() {
    int result = Objects.hashCode(message);
    result = 31 * result + Objects.hashCode(requestId);
    result = 31 * result + code;
    return result;
  }
}
