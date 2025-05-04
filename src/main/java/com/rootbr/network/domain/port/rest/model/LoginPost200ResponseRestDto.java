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
 * LoginPost200ResponseRestDto
 */

@JsonTypeName("_login_post_200_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class LoginPost200ResponseRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String token;

  public LoginPost200ResponseRestDto token(String token) {
    this.token = token;
    return this;
  }

  /**
   * Get token
   * @return token
   */
  
  @JsonProperty("token")
  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginPost200ResponseRestDto loginPost200Response = (LoginPost200ResponseRestDto) o;
    return Objects.equals(this.token, loginPost200Response.token);
  }

  @Override
  public int hashCode() {
    return Objects.hash(token);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginPost200ResponseRestDto {\n");
    sb.append("    token: ").append(toIndentedString(token)).append("\n");
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

    private LoginPost200ResponseRestDto instance;

    public Builder() {
      this(new LoginPost200ResponseRestDto());
    }

    protected Builder(LoginPost200ResponseRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(LoginPost200ResponseRestDto value) { 
      this.instance.setToken(value.token);
      return this;
    }

    public LoginPost200ResponseRestDto.Builder token(String token) {
      this.instance.token(token);
      return this;
    }
    
    /**
    * returns a built LoginPost200ResponseRestDto instance.
    *
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

  /**
  * Create a builder with no initialized field (except for the default values).
  */
  public static LoginPost200ResponseRestDto.Builder builder() {
    return new LoginPost200ResponseRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public LoginPost200ResponseRestDto.Builder toBuilder() {
    LoginPost200ResponseRestDto.Builder builder = new LoginPost200ResponseRestDto.Builder();
    return builder.copyOf(this);
  }

}

