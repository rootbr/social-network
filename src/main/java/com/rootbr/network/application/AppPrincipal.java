package com.rootbr.network.application;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Objects;

public class AppPrincipal implements Principal {
  private final String id;
  private final SocialNetworkApplication application;

  public AppPrincipal(final String id, final SocialNetworkApplication application) {
    this.id = id;
    this.application = application;
  }

  public void registerUser(String userId, String firstName, String lastName, String city, LocalDate birthdate, String biography) {
    application.registerUser(this, userId, firstName, lastName, city, birthdate, biography);
  }


  @Override
  public boolean equals(final Object o) {
    if (!(o instanceof final AppPrincipal that)) {
      return false;
    }
    return Objects.equals(id, that.id);
  }

  @Override
  public String toString() {
    return id;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String getName() {
    return id;
  }
}
