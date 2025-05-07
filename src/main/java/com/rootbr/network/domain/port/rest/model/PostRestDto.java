package com.rootbr.network.domain.port.rest.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Пост пользователя
 */



public class PostRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String text;

  private String authorUserId;

  /**
   * Create a builder with no initialized field (except for the default values).
   */
  public static PostRestDto.Builder builder() {
    return new PostRestDto.Builder();
  }

  public PostRestDto id(final String id) {
    this.id = id;
    return this;
  }

  /**
   * Идентификатор поста
   *
   * @return id
   */


  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public PostRestDto text(final String text) {
    this.text = text;
    return this;
  }

  /**
   * Текст поста
   *
   * @return text
   */


  public String getText() {
    return text;
  }

  public void setText(final String text) {
    this.text = text;
  }

  public PostRestDto authorUserId(final String authorUserId) {
    this.authorUserId = authorUserId;
    return this;
  }

  /**
   * Идентификатор пользователя
   *
   * @return authorUserId
   */


  public String getAuthorUserId() {
    return authorUserId;
  }

  public void setAuthorUserId(final String authorUserId) {
    this.authorUserId = authorUserId;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final PostRestDto post = (PostRestDto) o;
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
    final String sb = "class PostRestDto {\n"
        + "    id: " + toIndentedString(id) + "\n"
        + "    text: " + toIndentedString(text) + "\n"
        + "    authorUserId: " + toIndentedString(authorUserId) + "\n"
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
  public PostRestDto.Builder toBuilder() {
    final PostRestDto.Builder builder = new PostRestDto.Builder();
    return builder.copyOf(this);
  }

  public static class Builder {

    private PostRestDto instance;

    public Builder() {
      this(new PostRestDto());
    }

    protected Builder(final PostRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(final PostRestDto value) {
      this.instance.setId(value.id);
      this.instance.setText(value.text);
      this.instance.setAuthorUserId(value.authorUserId);
      return this;
    }

    public PostRestDto.Builder id(final String id) {
      this.instance.id(id);
      return this;
    }

    public PostRestDto.Builder text(final String text) {
      this.instance.text(text);
      return this;
    }

    public PostRestDto.Builder authorUserId(final String authorUserId) {
      this.instance.authorUserId(authorUserId);
      return this;
    }

    /**
     * returns a built PostRestDto instance.
     * <p>
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

}

