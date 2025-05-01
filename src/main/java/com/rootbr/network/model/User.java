package com.rootbr.network.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private String id;
  private String firstName;
  private String secondName;
  private LocalDate birthdate;
  private String biography;
  private String city;
}
