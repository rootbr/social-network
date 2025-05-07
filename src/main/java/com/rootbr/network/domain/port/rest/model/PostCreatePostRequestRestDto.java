package com.rootbr.network.domain.port.rest.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * PostCreatePostRequestRestDto
 */



public class PostCreatePostRequestRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String text;

  public PostCreatePostRequestRestDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PostCreatePostRequestRestDto(final String text) {
    this.text = text;
  }

  /**
   * Create a builder with no initialized field (except for the default values).
   */
  public static PostCreatePostRequestRestDto.Builder builder() {
    return new PostCreatePostRequestRestDto.Builder();
  }

  public PostCreatePostRequestRestDto text(final String text) {
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

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final PostCreatePostRequestRestDto postCreatePostRequest = (PostCreatePostRequestRestDto) o;
    return Objects.equals(this.text, postCreatePostRequest.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }

  @Override
  public String toString() {
    final String sb = "class PostCreatePostRequestRestDto {\n"
        + "    text: " + toIndentedString(text) + "\n"
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
  public PostCreatePostRequestRestDto.Builder toBuilder() {
    final PostCreatePostRequestRestDto.Builder builder = new PostCreatePostRequestRestDto.Builder();
    return builder.copyOf(this);
  }

  public static class Builder {

    private PostCreatePostRequestRestDto instance;

    public Builder() {
      this(new PostCreatePostRequestRestDto());
    }

    protected Builder(final PostCreatePostRequestRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(final PostCreatePostRequestRestDto value) {
      this.instance.setText(value.text);
      return this;
    }

    public PostCreatePostRequestRestDto.Builder text(final String text) {
      this.instance.text(text);
      return this;
    }

    /**
     * returns a built PostCreatePostRequestRestDto instance.
     * <p>
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

}

