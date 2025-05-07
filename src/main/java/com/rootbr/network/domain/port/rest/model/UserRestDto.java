package com.rootbr.network.domain.port.rest.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * UserRestDto
 */

public class UserRestDto implements Serializable {

  private static final long serialVersionUID = 1L;

  private String id;

  private String firstName;

  private String secondName;


  private LocalDate birthdate;

  private String biography;

  private String city;

  /**
   * Create a builder with no initialized field (except for the default values).
   */
  public static UserRestDto.Builder builder() {
    return new UserRestDto.Builder();
  }

  public UserRestDto id(final String id) {
    this.id = id;
    return this;
  }

  /**
   * Идентификатор пользователя
   *
   * @return id
   */


  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public UserRestDto firstName(final String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Имя
   *
   * @return firstName
   */


  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(final String firstName) {
    this.firstName = firstName;
  }

  public UserRestDto secondName(final String secondName) {
    this.secondName = secondName;
    return this;
  }

  /**
   * Фамилия
   *
   * @return secondName
   */


  public String getSecondName() {
    return secondName;
  }

  public void setSecondName(final String secondName) {
    this.secondName = secondName;
  }

  public UserRestDto birthdate(final LocalDate birthdate) {
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

  public UserRestDto biography(final String biography) {
    this.biography = biography;
    return this;
  }

  /**
   * Интересы
   *
   * @return biography
   */


  public String getBiography() {
    return biography;
  }

  public void setBiography(final String biography) {
    this.biography = biography;
  }

  public UserRestDto city(final String city) {
    this.city = city;
    return this;
  }

  /**
   * Город
   *
   * @return city
   */


  public String getCity() {
    return city;
  }

  public void setCity(final String city) {
    this.city = city;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final UserRestDto user = (UserRestDto) o;
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
    final String sb = "class UserRestDto {\n"
        + "    id: " + toIndentedString(id) + "\n"
        + "    firstName: " + toIndentedString(firstName) + "\n"
        + "    secondName: " + toIndentedString(secondName) + "\n"
        + "    birthdate: " + toIndentedString(birthdate) + "\n"
        + "    biography: " + toIndentedString(biography) + "\n"
        + "    city: " + toIndentedString(city) + "\n"
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
  public UserRestDto.Builder toBuilder() {
    final UserRestDto.Builder builder = new UserRestDto.Builder();
    return builder.copyOf(this);
  }

  public static class Builder {

    private UserRestDto instance;

    public Builder() {
      this(new UserRestDto());
    }

    protected Builder(final UserRestDto instance) {
      this.instance = instance;
    }

    protected Builder copyOf(final UserRestDto value) {
      this.instance.setId(value.id);
      this.instance.setFirstName(value.firstName);
      this.instance.setSecondName(value.secondName);
      this.instance.setBirthdate(value.birthdate);
      this.instance.setBiography(value.biography);
      this.instance.setCity(value.city);
      return this;
    }

    public UserRestDto.Builder id(final String id) {
      this.instance.id(id);
      return this;
    }

    public UserRestDto.Builder firstName(final String firstName) {
      this.instance.firstName(firstName);
      return this;
    }

    public UserRestDto.Builder secondName(final String secondName) {
      this.instance.secondName(secondName);
      return this;
    }

    public UserRestDto.Builder birthdate(final LocalDate birthdate) {
      this.instance.birthdate(birthdate);
      return this;
    }

    public UserRestDto.Builder biography(final String biography) {
      this.instance.biography(biography);
      return this;
    }

    public UserRestDto.Builder city(final String city) {
      this.instance.city(city);
      return this;
    }

    /**
     * returns a built UserRestDto instance.
     * <p>
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

}

