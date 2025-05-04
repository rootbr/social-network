package com.rootbr.network.domain.port.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import org.springframework.lang.Nullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;


import java.util.*;
import javax.annotation.Generated;

/**
 * UserRegisterPost200ResponseRestDto
 */

@JsonTypeName("_user_register_post_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class UserRegisterPost200ResponseRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String userId;

  public UserRegisterPost200ResponseRestDto userId(String userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Get userId
   * @return userId
   */
  
  @JsonProperty("user_id")
  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRegisterPost200ResponseRestDto userRegisterPost200Response = (UserRegisterPost200ResponseRestDto) o;
    return Objects.equals(this.userId, userRegisterPost200Response.userId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRegisterPost200ResponseRestDto {\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
  public static class Builder {

    private UserRegisterPost200ResponseRestDto instance;

    public Builder() {
      this(new UserRegisterPost200ResponseRestDto());
    }

    protected Builder(UserRegisterPost200ResponseRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(UserRegisterPost200ResponseRestDto value) { 
      this.instance.setUserId(value.userId);
      return this;
    }

    public UserRegisterPost200ResponseRestDto.Builder userId(String userId) {
      this.instance.userId(userId);
      return this;
    }
    
    /**
    * returns a built UserRegisterPost200ResponseRestDto instance.
    *
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

  /**
  * Create a builder with no initialized field (except for the default values).
  */
  public static UserRegisterPost200ResponseRestDto.Builder builder() {
    return new UserRegisterPost200ResponseRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public UserRegisterPost200ResponseRestDto.Builder toBuilder() {
    UserRegisterPost200ResponseRestDto.Builder builder = new UserRegisterPost200ResponseRestDto.Builder();
    return builder.copyOf(this);
  }

}

