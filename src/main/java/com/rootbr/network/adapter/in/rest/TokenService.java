package com.rootbr.network.adapter.in.rest;

import com.rootbr.network.application.Principal;
import java.util.function.BiFunction;

public interface TokenService {

  String generateToken(String principalId, String email);

  boolean validateToken(String jwt);

  Principal createPrincipal(String jwt, BiFunction<String, String, Principal> principalFactory);
}
