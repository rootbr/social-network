package com.rootbr.network.domain.port.rest.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * LoginPost200ResponseRestDto
 */


public class LoginPost200ResponseRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String token;

  /**
   * Create a builder with no initialized field (except for the default values).
   */
  public static LoginPost200ResponseRestDto.Builder builder() {
    return new LoginPost200ResponseRestDto.Builder();
  }

  public LoginPost200ResponseRestDto token(final String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   *
   * @return token
   */


  public String getToken() {
    return token;
  }

  public void setToken(final String token) {
    this.token = token;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final LoginPost200ResponseRestDto loginPost200Response = (LoginPost200ResponseRestDto) o;
    return Objects.equals(this.token, loginPost200Response.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

  @Override
  public String toString() {
    final String sb = "class LoginPost200ResponseRestDto {\n"
        + "    token: " + toIndentedString(token) + "\n"
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
  public LoginPost200ResponseRestDto.Builder toBuilder() {
    final LoginPost200ResponseRestDto.Builder builder = new LoginPost200ResponseRestDto.Builder();
    return builder.copyOf(this);
  }

  public static class Builder {

    private LoginPost200ResponseRestDto instance;

    public Builder() {
      this(new LoginPost200ResponseRestDto());
    }

    protected Builder(final LoginPost200ResponseRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(final LoginPost200ResponseRestDto value) {
      this.instance.setToken(value.token);
      return this;
    }

    public LoginPost200ResponseRestDto.Builder token(final String token) {
      this.instance.token(token);
      return this;
    }

    /**
     * returns a built LoginPost200ResponseRestDto instance.
     * <p>
     * The builder is not reusable (NullPointerException)
     */
    public LoginPost200ResponseRestDto build() {
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

