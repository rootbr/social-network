package com.rootbr.network.domain.port.rest.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * UserRegisterPost200ResponseRestDto
 */



public class UserRegisterPost200ResponseRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String userId;

  /**
   * Create a builder with no initialized field (except for the default values).
   */
  public static UserRegisterPost200ResponseRestDto.Builder builder() {
    return new UserRegisterPost200ResponseRestDto.Builder();
  }

  public UserRegisterPost200ResponseRestDto userId(final String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   *
   * @return userId
   */


  public String getUserId() {
    return userId;
  }

  public void setUserId(final String userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final UserRegisterPost200ResponseRestDto userRegisterPost200Response = (UserRegisterPost200ResponseRestDto) o;
    return Objects.equals(this.userId, userRegisterPost200Response.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }

  @Override
  public String toString() {
    final String sb = "class UserRegisterPost200ResponseRestDto {\n"
        + "    userId: " + toIndentedString(userId) + "\n"
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
  public UserRegisterPost200ResponseRestDto.Builder toBuilder() {
    final UserRegisterPost200ResponseRestDto.Builder builder = new UserRegisterPost200ResponseRestDto.Builder();
    return builder.copyOf(this);
  }

  public static class Builder {

    private UserRegisterPost200ResponseRestDto instance;

    public Builder() {
      this(new UserRegisterPost200ResponseRestDto());
    }

    protected Builder(final UserRegisterPost200ResponseRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(final UserRegisterPost200ResponseRestDto value) {
      this.instance.setUserId(value.userId);
      return this;
    }

    public UserRegisterPost200ResponseRestDto.Builder userId(final String userId) {
      this.instance.userId(userId);
      return this;
    }

    /**
     * returns a built UserRegisterPost200ResponseRestDto instance.
     * <p>
     * The builder is not reusable (NullPointerException)
     */
    public UserRegisterPost200ResponseRestDto build() {
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

