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
 * LoginPost500ResponseRestDto
 */

@JsonTypeName("_login_post_500_response")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class LoginPost500ResponseRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String message;

  private @Nullable String requestId;

  private @Nullable Integer code;

  public LoginPost500ResponseRestDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public LoginPost500ResponseRestDto(String message) {
    this.message = message;
  }

  public LoginPost500ResponseRestDto message(String message) {
    this.message = message;
    return this;
  }

  /**
   * Описание ошибки
   * @return message
   */
  @NotNull 
  @JsonProperty("message")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public LoginPost500ResponseRestDto requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

  /**
   * Идентификатор запроса. Предназначен для более быстрого поиска проблем.
   * @return requestId
   */
  
  @JsonProperty("request_id")
  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public LoginPost500ResponseRestDto code(Integer code) {
    this.code = code;
    return this;
  }

  /**
   * Код ошибки. Предназначен для классификации проблем и более быстрого решения проблем.
   * @return code
   */
  
  @JsonProperty("code")
  public Integer getCode() {
    return code;
  }

  public void setCode(Integer code) {
    this.code = code;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LoginPost500ResponseRestDto loginPost500Response = (LoginPost500ResponseRestDto) o;
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
    StringBuilder sb = new StringBuilder();
    sb.append("class LoginPost500ResponseRestDto {\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    requestId: ").append(toIndentedString(requestId)).append("\n");
    sb.append("    code: ").append(toIndentedString(code)).append("\n");
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

    private LoginPost500ResponseRestDto instance;

    public Builder() {
      this(new LoginPost500ResponseRestDto());
    }

    protected Builder(LoginPost500ResponseRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(LoginPost500ResponseRestDto value) { 
      this.instance.setMessage(value.message);
      this.instance.setRequestId(value.requestId);
      this.instance.setCode(value.code);
      return this;
    }

    public LoginPost500ResponseRestDto.Builder message(String message) {
      this.instance.message(message);
      return this;
    }
    
    public LoginPost500ResponseRestDto.Builder requestId(String requestId) {
      this.instance.requestId(requestId);
      return this;
    }
    
    public LoginPost500ResponseRestDto.Builder code(Integer code) {
      this.instance.code(code);
      return this;
    }
    
    /**
    * returns a built LoginPost500ResponseRestDto instance.
    *
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

  /**
  * Create a builder with no initialized field (except for the default values).
  */
  public static LoginPost500ResponseRestDto.Builder builder() {
    return new LoginPost500ResponseRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public LoginPost500ResponseRestDto.Builder toBuilder() {
    LoginPost500ResponseRestDto.Builder builder = new LoginPost500ResponseRestDto.Builder();
    return builder.copyOf(this);
  }

}

