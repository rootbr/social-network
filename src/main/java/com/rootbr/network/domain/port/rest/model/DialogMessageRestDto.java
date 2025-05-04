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
 * DialogMessageRestDto
 */

@JsonTypeName("DialogMessage")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class DialogMessageRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String from;

  private String to;

  private String text;

  public DialogMessageRestDto() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DialogMessageRestDto(String from, String to, String text) {
    this.from = from;
    this.to = to;
    this.text = text;
  }

  public DialogMessageRestDto from(String from) {
    this.from = from;
    return this;
  }

  /**
   * Идентификатор пользователя
   * @return from
   */
  @NotNull 
  @JsonProperty("from")
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public DialogMessageRestDto to(String to) {
    this.to = to;
    return this;
  }

  /**
   * Идентификатор пользователя
   * @return to
   */
  @NotNull 
  @JsonProperty("to")
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public DialogMessageRestDto text(String text) {
    this.text = text;
    return this;
  }

  /**
   * Текст сообщения
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
    DialogMessageRestDto dialogMessage = (DialogMessageRestDto) o;
    return Objects.equals(this.from, dialogMessage.from) &&
        Objects.equals(this.to, dialogMessage.to) &&
        Objects.equals(this.text, dialogMessage.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, text);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DialogMessageRestDto {\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
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

    private DialogMessageRestDto instance;

    public Builder() {
      this(new DialogMessageRestDto());
    }

    protected Builder(DialogMessageRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(DialogMessageRestDto value) { 
      this.instance.setFrom(value.from);
      this.instance.setTo(value.to);
      this.instance.setText(value.text);
      return this;
    }

    public DialogMessageRestDto.Builder from(String from) {
      this.instance.from(from);
      return this;
    }
    
    public DialogMessageRestDto.Builder to(String to) {
      this.instance.to(to);
      return this;
    }
    
    public DialogMessageRestDto.Builder text(String text) {
      this.instance.text(text);
      return this;
    }
    
    /**
    * returns a built DialogMessageRestDto instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public DialogMessageRestDto build() {
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
  public static DialogMessageRestDto.Builder builder() {
    return new DialogMessageRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public DialogMessageRestDto.Builder toBuilder() {
    DialogMessageRestDto.Builder builder = new DialogMessageRestDto.Builder();
    return builder.copyOf(this);
  }

}

