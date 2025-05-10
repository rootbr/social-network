package com.rootbr.network.application.usecase;

import java.io.IOException;

public interface LoginUseCase {

  boolean login(String jwt) throws IOException;
}
