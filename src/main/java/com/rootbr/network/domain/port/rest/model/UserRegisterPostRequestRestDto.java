package com.rootbr.network.domain.port.rest.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * UserRegisterPostRequestRestDto
 */


public class UserRegisterPostRequestRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String firstName;

  private String secondName;


  private LocalDate birthdate;

  private String biography;

  private String city;

  private String password;

  /**
   * Create a builder with no initialized field (except for the default values).
   */
  public static UserRegisterPostRequestRestDto.Builder builder() {
    return new UserRegisterPostRequestRestDto.Builder();
  }

  public UserRegisterPostRequestRestDto firstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   *
   * @return firstName
   */


  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public UserRegisterPostRequestRestDto secondName(final String secondName) {
    this.secondName = secondName;
    return this;
  }

  /**
   * Get secondName
   *
   * @return secondName
   */


  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(final String secondName) {
    this.secondName = secondName;
  }

  public UserRegisterPostRequestRestDto birthdate(final LocalDate birthdate) {
    this.birthdate = birthdate;
    return this;
  }

  /**
   * Дата рождения
   *
   * @return birthdate
   */


  public LocalDate getBirthdate() {
    return birthdate;
  }

  public void setBirthdate(final LocalDate birthdate) {
    this.birthdate = birthdate;
  }

  public UserRegisterPostRequestRestDto biography(final String biography) {
    this.biography = biography;
    return this;
  }

  /**
   * Get biography
   *
   * @return biography
   */


  public String getBiography() {
    return biography;
  }

  public void setBiography(final String biography) {
    this.biography = biography;
  }

  public UserRegisterPostRequestRestDto city(final String city) {
    this.city = city;
    return this;
  }

  /**
   * Get city
   *
   * @return city
   */


  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  public UserRegisterPostRequestRestDto password(final String password) {
    this.password = password;
    return this;
  }

  /**
   * Get password
   *
   * @return password
   */


  public String getPassword() {
    return password;
  }

  public void setPassword(final String password) {
    this.password = password;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final UserRegisterPostRequestRestDto userRegisterPostRequest = (UserRegisterPostRequestRestDto) o;
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
    final String sb = "class UserRegisterPostRequestRestDto {\n"
        + "    firstName: " + toIndentedString(firstName) + "\n"
        + "    secondName: " + toIndentedString(secondName) + "\n"
        + "    birthdate: " + toIndentedString(birthdate) + "\n"
        + "    biography: " + toIndentedString(biography) + "\n"
        + "    city: " + toIndentedString(city) + "\n"
        + "    password: " + toIndentedString(password) + "\n"
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
  public UserRegisterPostRequestRestDto.Builder toBuilder() {
    final UserRegisterPostRequestRestDto.Builder builder = new UserRegisterPostRequestRestDto.Builder();
    return builder.copyOf(this);
  }

  public static class Builder {

    private UserRegisterPostRequestRestDto instance;

    public Builder() {
      this(new UserRegisterPostRequestRestDto());
    }

    protected Builder(final UserRegisterPostRequestRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(final UserRegisterPostRequestRestDto value) {
      this.instance.setFirstName(value.firstName);
      this.instance.setSecondName(value.secondName);
      this.instance.setBirthdate(value.birthdate);
      this.instance.setBiography(value.biography);
      this.instance.setCity(value.city);
      this.instance.setPassword(value.password);
      return this;
    }

    public UserRegisterPostRequestRestDto.Builder firstName(final String firstName) {
      this.instance.firstName(firstName);
      return this;
    }

    public UserRegisterPostRequestRestDto.Builder secondName(final String secondName) {
      this.instance.secondName(secondName);
      return this;
    }

    public UserRegisterPostRequestRestDto.Builder birthdate(final LocalDate birthdate) {
      this.instance.birthdate(birthdate);
      return this;
    }

    public UserRegisterPostRequestRestDto.Builder biography(final String biography) {
      this.instance.biography(biography);
      return this;
    }

    public UserRegisterPostRequestRestDto.Builder city(final String city) {
      this.instance.city(city);
      return this;
    }

    public UserRegisterPostRequestRestDto.Builder password(final String password) {
      this.instance.password(password);
      return this;
    }

    /**
     * returns a built UserRegisterPostRequestRestDto instance.
     * <p>
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

}

