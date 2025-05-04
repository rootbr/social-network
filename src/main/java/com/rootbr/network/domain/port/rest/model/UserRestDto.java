package com.rootbr.network.domain.port.rest.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;
import java.io.Serializable;
import java.time.OffsetDateTime;
import javax.validation.Valid;
import javax.validation.constraints.*;


import java.util.*;
import javax.annotation.Generated;

/**
 * UserRestDto
 */

@JsonTypeName("User")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class UserRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String id;

  private @Nullable String firstName;

  private @Nullable String secondName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate birthdate;

  private @Nullable String biography;

  private @Nullable String city;

  public UserRestDto id(String id) {
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

  public UserRestDto firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Имя
   * @return firstName
   */
  
  @JsonProperty("first_name")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public UserRestDto secondName(String secondName) {
    this.secondName = secondName;
    return this;
  }

  /**
   * Фамилия
   * @return secondName
   */
  
  @JsonProperty("second_name")
  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public UserRestDto birthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
    return this;
  }

  /**
   * Дата рождения
   * @return birthdate
   */
  @Valid 
  @JsonProperty("birthdate")
  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public UserRestDto biography(String biography) {
    this.biography = biography;
    return this;
  }

  /**
   * Интересы
   * @return biography
   */
  
  @JsonProperty("biography")
  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public UserRestDto city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Город
   * @return city
   */
  
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserRestDto user = (UserRestDto) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.firstName, user.firstName) &&
        Objects.equals(this.secondName, user.secondName) &&
        Objects.equals(this.birthdate, user.birthdate) &&
        Objects.equals(this.biography, user.biography) &&
        Objects.equals(this.city, user.city);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, secondName, birthdate, biography, city);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRestDto {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    secondName: ").append(toIndentedString(secondName)).append("\n");
    sb.append("    birthdate: ").append(toIndentedString(birthdate)).append("\n");
    sb.append("    biography: ").append(toIndentedString(biography)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
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

    private UserRestDto instance;

    public Builder() {
      this(new UserRestDto());
    }

    protected Builder(UserRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(UserRestDto value) { 
      this.instance.setId(value.id);
      this.instance.setFirstName(value.firstName);
      this.instance.setSecondName(value.secondName);
      this.instance.setBirthdate(value.birthdate);
      this.instance.setBiography(value.biography);
      this.instance.setCity(value.city);
      return this;
    }

    public UserRestDto.Builder id(String id) {
      this.instance.id(id);
      return this;
    }
    
    public UserRestDto.Builder firstName(String firstName) {
      this.instance.firstName(firstName);
      return this;
    }
    
    public UserRestDto.Builder secondName(String secondName) {
      this.instance.secondName(secondName);
      return this;
    }
    
    public UserRestDto.Builder birthdate(LocalDate birthdate) {
      this.instance.birthdate(birthdate);
      return this;
    }
    
    public UserRestDto.Builder biography(String biography) {
      this.instance.biography(biography);
      return this;
    }
    
    public UserRestDto.Builder city(String city) {
      this.instance.city(city);
      return this;
    }
    
    /**
    * returns a built UserRestDto instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public UserRestDto build() {
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
  public static UserRestDto.Builder builder() {
    return new UserRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public UserRestDto.Builder toBuilder() {
    UserRestDto.Builder builder = new UserRestDto.Builder();
    return builder.copyOf(this);
  }

}

