package com.sourcecode.infrastructure.services;

import com.sourcecode.models.UserAccount;

public interface UserAccountService {
    UserAccount createUserAccount(UserAccount userAccount);

    UserAccount findUserAccount(String username);
    UserAccount findUserAccount(Long id);

}
