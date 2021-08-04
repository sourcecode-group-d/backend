package com.sourcecode.infrastructure.services;

import com.sourcecode.models.UserAccount;

import java.util.List;

public interface UserAccountService {
    UserAccount createUserAccount(UserAccount userAccount);
    UserAccount findUserAccount(String username);
    UserAccount findUserAccount(Long id);
    UserAccount deleteUserAccount(String username);

    List<UserAccount> findAllUsers();
}
