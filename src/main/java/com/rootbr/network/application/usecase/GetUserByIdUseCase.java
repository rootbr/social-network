package com.rootbr.network.application.usecase;

import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.port.rest.model.UserRestDto;
import java.io.IOException;

public interface GetUserByIdUseCase {

  public void getUserById(final CommandAuthor commandAuthor, final String id,
      final UserRestDto.Builder response)
      throws IOException;
}
