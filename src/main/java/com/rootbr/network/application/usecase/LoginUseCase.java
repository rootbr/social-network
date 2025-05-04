package com.rootbr.network.application.usecase;

import com.rootbr.network.domain.port.rest.model.LoginPostRequestRestDto;

public interface LoginUseCase {

  boolean login(LoginPostRequestRestDto request);
}
