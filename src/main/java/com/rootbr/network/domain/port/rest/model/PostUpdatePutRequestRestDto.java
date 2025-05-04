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
 * PostUpdatePutRequestRestDto
 */

@JsonTypeName("_post_update_put_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class PostUpdatePutRequestRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String text;

  public PostUpdatePutRequestRestDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PostUpdatePutRequestRestDto(String id, String text) {
    this.id = id;
    this.text = text;
  }

  public PostUpdatePutRequestRestDto id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Идентификатор поста
   * @return id
   */
  @NotNull 
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PostUpdatePutRequestRestDto text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Текст поста
   * @return text
   */
  @NotNull 
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostUpdatePutRequestRestDto postUpdatePutRequest = (PostUpdatePutRequestRestDto) o;
    return Objects.equals(this.id, postUpdatePutRequest.id) &&
        Objects.equals(this.text, postUpdatePutRequest.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostUpdatePutRequestRestDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
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

    private PostUpdatePutRequestRestDto instance;

    public Builder() {
      this(new PostUpdatePutRequestRestDto());
    }

    protected Builder(PostUpdatePutRequestRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(PostUpdatePutRequestRestDto value) { 
      this.instance.setId(value.id);
      this.instance.setText(value.text);
      return this;
    }

    public PostUpdatePutRequestRestDto.Builder id(String id) {
      this.instance.id(id);
      return this;
    }
    
    public PostUpdatePutRequestRestDto.Builder text(String text) {
      this.instance.text(text);
      return this;
    }
    
    /**
    * returns a built PostUpdatePutRequestRestDto instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public PostUpdatePutRequestRestDto build() {
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
  public static PostUpdatePutRequestRestDto.Builder builder() {
    return new PostUpdatePutRequestRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public PostUpdatePutRequestRestDto.Builder toBuilder() {
    PostUpdatePutRequestRestDto.Builder builder = new PostUpdatePutRequestRestDto.Builder();
    return builder.copyOf(this);
  }

}

