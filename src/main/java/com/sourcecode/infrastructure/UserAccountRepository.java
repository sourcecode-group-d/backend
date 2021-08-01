package com.sourcecode.infrastructure;

import com.sourcecode.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount , Long> {
    UserAccount findUserAccountByUsername(String username) ;
}
