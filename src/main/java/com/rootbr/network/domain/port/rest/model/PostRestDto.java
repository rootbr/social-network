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
 * Пост пользователя
 */

@JsonTypeName("Post")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class PostRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String id;

  private @Nullable String text;

  private @Nullable String authorUserId;

  public PostRestDto id(String id) {
    this.id = id;
    return this;
  }

  /**
   * Идентификатор поста
   * @return id
   */
  
  @JsonProperty("id")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public PostRestDto text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Текст поста
   * @return text
   */
  
  @JsonProperty("text")
  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public PostRestDto authorUserId(String authorUserId) {
    this.authorUserId = authorUserId;
    return this;
  }

  /**
   * Идентификатор пользователя
   * @return authorUserId
   */
  
  @JsonProperty("author_user_id")
  public String getAuthorUserId() {
    return authorUserId;
  }

  public void setAuthorUserId(String authorUserId) {
    this.authorUserId = authorUserId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostRestDto post = (PostRestDto) o;
    return Objects.equals(this.id, post.id) &&
        Objects.equals(this.text, post.text) &&
        Objects.equals(this.authorUserId, post.authorUserId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text, authorUserId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PostRestDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    authorUserId: ").append(toIndentedString(authorUserId)).append("\n");
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

    private PostRestDto instance;

    public Builder() {
      this(new PostRestDto());
    }

    protected Builder(PostRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(PostRestDto value) { 
      this.instance.setId(value.id);
      this.instance.setText(value.text);
      this.instance.setAuthorUserId(value.authorUserId);
      return this;
    }

    public PostRestDto.Builder id(String id) {
      this.instance.id(id);
      return this;
    }
    
    public PostRestDto.Builder text(String text) {
      this.instance.text(text);
      return this;
    }
    
    public PostRestDto.Builder authorUserId(String authorUserId) {
      this.instance.authorUserId(authorUserId);
      return this;
    }
    
    /**
    * returns a built PostRestDto instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public PostRestDto build() {
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
  public static PostRestDto.Builder builder() {
    return new PostRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public PostRestDto.Builder toBuilder() {
    PostRestDto.Builder builder = new PostRestDto.Builder();
    return builder.copyOf(this);
  }

}

