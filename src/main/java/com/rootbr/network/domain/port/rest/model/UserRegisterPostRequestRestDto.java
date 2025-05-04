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
 * UserRegisterPostRequestRestDto
 */

@JsonTypeName("_user_register_post_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-04T22:50:29.295949+02:00[Europe/Berlin]", comments = "Generator version: 7.13.0")
public class UserRegisterPostRequestRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private @Nullable String firstName;

  private @Nullable String secondName;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private @Nullable LocalDate birthdate;

  private @Nullable String biography;

  private @Nullable String city;

  private @Nullable String password;

  public UserRegisterPostRequestRestDto firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  
  @JsonProperty("first_name")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public UserRegisterPostRequestRestDto secondName(String secondName) {
    this.secondName = secondName;
    return this;
  }

  /**
   * Get secondName
   * @return secondName
   */
  
  @JsonProperty("second_name")
  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(String secondName) {
    this.secondName = secondName;
  }

  public UserRegisterPostRequestRestDto birthdate(LocalDate birthdate) {
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

  public UserRegisterPostRequestRestDto biography(String biography) {
    this.biography = biography;
    return this;
  }

  /**
   * Get biography
   * @return biography
   */
  
  @JsonProperty("biography")
  public String getBiography() {
    return biography;
  }

  public void setBiography(String biography) {
    this.biography = biography;
  }

  public UserRegisterPostRequestRestDto city(String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   * @return city
   */
  
  @JsonProperty("city")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public UserRegisterPostRequestRestDto password(String password) {
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
    UserRegisterPostRequestRestDto userRegisterPostRequest = (UserRegisterPostRequestRestDto) o;
    return Objects.equals(this.firstName, userRegisterPostRequest.firstName) &&
        Objects.equals(this.secondName, userRegisterPostRequest.secondName) &&
        Objects.equals(this.birthdate, userRegisterPostRequest.birthdate) &&
        Objects.equals(this.biography, userRegisterPostRequest.biography) &&
        Objects.equals(this.city, userRegisterPostRequest.city) &&
        Objects.equals(this.password, userRegisterPostRequest.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, secondName, birthdate, biography, city, password);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UserRegisterPostRequestRestDto {\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    secondName: ").append(toIndentedString(secondName)).append("\n");
    sb.append("    birthdate: ").append(toIndentedString(birthdate)).append("\n");
    sb.append("    biography: ").append(toIndentedString(biography)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
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

    private UserRegisterPostRequestRestDto instance;

    public Builder() {
      this(new UserRegisterPostRequestRestDto());
    }

    protected Builder(UserRegisterPostRequestRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(UserRegisterPostRequestRestDto value) { 
      this.instance.setFirstName(value.firstName);
      this.instance.setSecondName(value.secondName);
      this.instance.setBirthdate(value.birthdate);
      this.instance.setBiography(value.biography);
      this.instance.setCity(value.city);
      this.instance.setPassword(value.password);
      return this;
    }

    public UserRegisterPostRequestRestDto.Builder firstName(String firstName) {
      this.instance.firstName(firstName);
      return this;
    }
    
    public UserRegisterPostRequestRestDto.Builder secondName(String secondName) {
      this.instance.secondName(secondName);
      return this;
    }
    
    public UserRegisterPostRequestRestDto.Builder birthdate(LocalDate birthdate) {
      this.instance.birthdate(birthdate);
      return this;
    }
    
    public UserRegisterPostRequestRestDto.Builder biography(String biography) {
      this.instance.biography(biography);
      return this;
    }
    
    public UserRegisterPostRequestRestDto.Builder city(String city) {
      this.instance.city(city);
      return this;
    }
    
    public UserRegisterPostRequestRestDto.Builder password(String password) {
      this.instance.password(password);
      return this;
    }
    
    /**
    * returns a built UserRegisterPostRequestRestDto instance.
    *
    * The builder is not reusable (NullPointerException)
    */
    public UserRegisterPostRequestRestDto build() {
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
  public static UserRegisterPostRequestRestDto.Builder builder() {
    return new UserRegisterPostRequestRestDto.Builder();
  }

  /**
  * Create a builder with a shallow copy of this instance.
  */
  public UserRegisterPostRequestRestDto.Builder toBuilder() {
    UserRegisterPostRequestRestDto.Builder builder = new UserRegisterPostRequestRestDto.Builder();
    return builder.copyOf(this);
  }

}

