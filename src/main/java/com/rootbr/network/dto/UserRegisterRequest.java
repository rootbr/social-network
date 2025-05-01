package com.rootbr.network.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserRegisterRequest {

  private String firstName;
  private String secondName;
  private LocalDate birthdate;
  private String biography;
  private String city;
  private String password;
}
