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
 * LoginPostRequestRestDto
 */

@JsonTypeName("_login_post_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class LoginPostRequestRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String id;

  private @Nullable String password;

  public LoginPostRequestRestDto id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Идентификатор пользователя
   * @return id
   */
  
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public LoginPostRequestRestDto password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   * @return password
   */
  
  @JsonProperty("password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginPostRequestRestDto loginPostRequest = (LoginPostRequestRestDto) o;
    return Objects.equals(this.id, loginPostRequest.id) &&
        Objects.equals(this.password, loginPostRequest.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, password);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginPostRequestRestDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
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

    private LoginPostRequestRestDto instance;

    public Builder() {
      this(new LoginPostRequestRestDto());
    }

    protected Builder(LoginPostRequestRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(LoginPostRequestRestDto value) { 
      this.instance.setId(value.id);
      this.instance.setPassword(value.password);
      return this;
    }

    public LoginPostRequestRestDto.Builder id(String id) {
      this.instance.id(id);
      return this;
    }
    
    public LoginPostRequestRestDto.Builder password(String password) {
      this.instance.password(password);
      return this;
    }
    
    /**
    * returns a built LoginPostRequestRestDto instance.
    *
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

  /**
  * Create a builder with no initialized field (except for the default values).
  */
  public static LoginPostRequestRestDto.Builder builder() {
    return new LoginPostRequestRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public LoginPostRequestRestDto.Builder toBuilder() {
    LoginPostRequestRestDto.Builder builder = new LoginPostRequestRestDto.Builder();
    return builder.copyOf(this);
  }

}

