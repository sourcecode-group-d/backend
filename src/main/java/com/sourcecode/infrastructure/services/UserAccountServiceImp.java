package com.sourcecode.infrastructure.services;

import com.sourcecode.infrastructure.UserAccountRepository;
import com.sourcecode.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImp implements UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository ;
    @Override
    public UserAccount createUserAccount(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount findUserAccount(String username) {
        return userAccountRepository.findUserAccountByUsername(username);
    }

    @Override
    public UserAccount findUserAccount(Long id) {
        return userAccountRepository.findById(id).orElseThrow();
    }

    @Override
    public UserAccount deleteUserAccount(String username) {
        UserAccount userAccount = userAccountRepository.findUserAccountByUsername(username);
        userAccountRepository.deleteById(userAccount.getId());
        return userAccount;
    }
}
