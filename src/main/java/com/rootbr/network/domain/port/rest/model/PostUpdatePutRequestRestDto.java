package com.rootbr.network.domain.port.rest.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * PostUpdatePutRequestRestDto
 */


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
  public PostUpdatePutRequestRestDto(final String id, final String text) {
    this.id = id;
    this.text = text;
  }

  /**
   * Create a builder with no initialized field (except for the default values).
   */
  public static PostUpdatePutRequestRestDto.Builder builder() {
    return new PostUpdatePutRequestRestDto.Builder();
  }

  public PostUpdatePutRequestRestDto id(final String id) {
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

  public PostUpdatePutRequestRestDto text(final String text) {
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
    final PostUpdatePutRequestRestDto postUpdatePutRequest = (PostUpdatePutRequestRestDto) o;
    return Objects.equals(this.id, postUpdatePutRequest.id) &&
        Objects.equals(this.text, postUpdatePutRequest.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, text);
  }

  @Override
  public String toString() {
    final String sb = "class PostUpdatePutRequestRestDto {\n"
        + "    id: " + toIndentedString(id) + "\n"
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
  public PostUpdatePutRequestRestDto.Builder toBuilder() {
    final PostUpdatePutRequestRestDto.Builder builder = new PostUpdatePutRequestRestDto.Builder();
    return builder.copyOf(this);
  }

  public static class Builder {

    private PostUpdatePutRequestRestDto instance;

    public Builder() {
      this(new PostUpdatePutRequestRestDto());
    }

    protected Builder(final PostUpdatePutRequestRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(final PostUpdatePutRequestRestDto value) {
      this.instance.setId(value.id);
      this.instance.setText(value.text);
      return this;
    }

    public PostUpdatePutRequestRestDto.Builder id(final String id) {
      this.instance.id(id);
      return this;
    }

    public PostUpdatePutRequestRestDto.Builder text(final String text) {
      this.instance.text(text);
      return this;
    }

    /**
     * returns a built PostUpdatePutRequestRestDto instance.
     * <p>
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

}

