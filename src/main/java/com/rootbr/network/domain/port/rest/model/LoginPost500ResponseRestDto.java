package com.rootbr.network.domain.port.rest.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * LoginPost500ResponseRestDto
 */


public class LoginPost500ResponseRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String message;

  private String requestId;

  private Integer code;

  public LoginPost500ResponseRestDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoginPost500ResponseRestDto(final String message) {
    this.message = message;
  }

  /**
   * Create a builder with no initialized field (except for the default values).
   */
  public static LoginPost500ResponseRestDto.Builder builder() {
    return new LoginPost500ResponseRestDto.Builder();
  }

  public LoginPost500ResponseRestDto message(final String message) {
    this.message = message;
    return this;
  }

  /**
   * Описание ошибки
   *
   * @return message
   */


  public String getMessage() {
    return message;
  }

  public void setMessage(final String message) {
    this.message = message;
  }

  public LoginPost500ResponseRestDto requestId(final String requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Идентификатор запроса. Предназначен для более быстрого поиска проблем.
   *
   * @return requestId
   */


  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(final String requestId) {
    this.requestId = requestId;
  }

  public LoginPost500ResponseRestDto code(final Integer code) {
    this.code = code;
    return this;
  }

  /**
   * Код ошибки. Предназначен для классификации проблем и более быстрого решения проблем.
   *
   * @return code
   */


  public Integer getCode() {
    return code;
  }

  public void setCode(final Integer code) {
    this.code = code;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final LoginPost500ResponseRestDto loginPost500Response = (LoginPost500ResponseRestDto) o;
    return Objects.equals(this.message, loginPost500Response.message) &&
        Objects.equals(this.requestId, loginPost500Response.requestId) &&
        Objects.equals(this.code, loginPost500Response.code);
  }

  @Override
  public int hashCode() {
    return Objects.hash(message, requestId, code);
  }

  @Override
  public String toString() {
    final String sb = "class LoginPost500ResponseRestDto {\n"
        + "    message: " + toIndentedString(message) + "\n"
        + "    requestId: " + toIndentedString(requestId) + "\n"
        + "    code: " + toIndentedString(code) + "\n"
        + "}";
    return sb;
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(final Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  /**
   * Create a builder with a shallow copy of this instance.
   */
  public LoginPost500ResponseRestDto.Builder toBuilder() {
    final LoginPost500ResponseRestDto.Builder builder = new LoginPost500ResponseRestDto.Builder();
    return builder.copyOf(this);
  }

  public static class Builder {

    private LoginPost500ResponseRestDto instance;

    public Builder() {
      this(new LoginPost500ResponseRestDto());
    }

    protected Builder(final LoginPost500ResponseRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(final LoginPost500ResponseRestDto value) {
      this.instance.setMessage(value.message);
      this.instance.setRequestId(value.requestId);
      this.instance.setCode(value.code);
      return this;
    }

    public LoginPost500ResponseRestDto.Builder message(final String message) {
      this.instance.message(message);
      return this;
    }

    public LoginPost500ResponseRestDto.Builder requestId(final String requestId) {
      this.instance.requestId(requestId);
      return this;
    }

    public LoginPost500ResponseRestDto.Builder code(final Integer code) {
      this.instance.code(code);
      return this;
    }

    /**
     * returns a built LoginPost500ResponseRestDto instance.
     * <p>
     * The builder is not reusable (NullPointerException)
     */
    public LoginPost500ResponseRestDto build() {
      try {
        return this.instance;
      } finally {
        // ensure that this.instance is not reused
        this.instance = null;
      }
    }

    @Override
    public String toString() {
      return getClass() + "=(" + instance + ")";
    }
  }

}

