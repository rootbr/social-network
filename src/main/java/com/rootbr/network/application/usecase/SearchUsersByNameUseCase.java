package com.rootbr.network.application.usecase;

import com.rootbr.network.domain.engine.CommandAuthor;
import com.rootbr.network.domain.port.rest.model.UserRestDto;
import java.util.List;

public interface SearchUsersByNameUseCase {

  public void searchUsers(final CommandAuthor commandAuthor, String firstName, String lastName, List<UserRestDto> response);
}
