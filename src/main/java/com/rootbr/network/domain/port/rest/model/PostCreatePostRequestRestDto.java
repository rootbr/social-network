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
 * PostCreatePostRequestRestDto
 */

@JsonTypeName("_post_create_post_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class PostCreatePostRequestRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String text;

  public PostCreatePostRequestRestDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PostCreatePostRequestRestDto(String text) {
    this.text = text;
  }

  public PostCreatePostRequestRestDto text(String text) {
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
    PostCreatePostRequestRestDto postCreatePostRequest = (PostCreatePostRequestRestDto) o;
    return Objects.equals(this.text, postCreatePostRequest.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostCreatePostRequestRestDto {\n");
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

    private PostCreatePostRequestRestDto instance;

    public Builder() {
      this(new PostCreatePostRequestRestDto());
    }

    protected Builder(PostCreatePostRequestRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(PostCreatePostRequestRestDto value) { 
      this.instance.setText(value.text);
      return this;
    }

    public PostCreatePostRequestRestDto.Builder text(String text) {
      this.instance.text(text);
      return this;
    }
    
    /**
    * returns a built PostCreatePostRequestRestDto instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public PostCreatePostRequestRestDto build() {
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
  public static PostCreatePostRequestRestDto.Builder builder() {
    return new PostCreatePostRequestRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public PostCreatePostRequestRestDto.Builder toBuilder() {
    PostCreatePostRequestRestDto.Builder builder = new PostCreatePostRequestRestDto.Builder();
    return builder.copyOf(this);
  }

}

