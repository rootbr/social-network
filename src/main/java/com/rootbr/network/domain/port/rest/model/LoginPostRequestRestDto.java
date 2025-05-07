package com.rootbr.network.domain.port.rest.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * LoginPostRequestRestDto
 */



public class LoginPostRequestRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String password;

  /**
   * Create a builder with no initialized field (except for the default values).
   */
  public static LoginPostRequestRestDto.Builder builder() {
    return new LoginPostRequestRestDto.Builder();
  }

  public LoginPostRequestRestDto id(final String id) {
    this.id = id;
    return this;
  }

  /**
   * Идентификатор пользователя
   *
   * @return id
   */


  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public LoginPostRequestRestDto password(final String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   *
   * @return password
   */


  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final LoginPostRequestRestDto loginPostRequest = (LoginPostRequestRestDto) o;
    return Objects.equals(this.id, loginPostRequest.id) &&
        Objects.equals(this.password, loginPostRequest.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, password);
  }

  @Override
  public String toString() {
    final String sb = "class LoginPostRequestRestDto {\n"
        + "    id: " + toIndentedString(id) + "\n"
        + "    password: " + toIndentedString(password) + "\n"
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
  public LoginPostRequestRestDto.Builder toBuilder() {
    final LoginPostRequestRestDto.Builder builder = new LoginPostRequestRestDto.Builder();
    return builder.copyOf(this);
  }

  public static class Builder {

    private LoginPostRequestRestDto instance;

    public Builder() {
      this(new LoginPostRequestRestDto());
    }

    protected Builder(final LoginPostRequestRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(final LoginPostRequestRestDto value) {
      this.instance.setId(value.id);
      this.instance.setPassword(value.password);
      return this;
    }

    public LoginPostRequestRestDto.Builder id(final String id) {
      this.instance.id(id);
      return this;
    }

    public LoginPostRequestRestDto.Builder password(final String password) {
      this.instance.password(password);
      return this;
    }

    /**
     * returns a built LoginPostRequestRestDto instance.
     * <p>
     * The builder is not reusable (NullPointerException)
     */
    public LoginPostRequestRestDto build() {
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

