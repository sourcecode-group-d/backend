package com.sourcecode;



import com.sourcecode.infrastructure.services.UserAccountService;
import com.sourcecode.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SourcecodeApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(SourcecodeApplication.class, args);
    }


    private final UserAccountService userAccountService ;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;

    SourcecodeApplication(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public void run(String... args) throws Exception {
        UserAccount userAccount = new UserAccount("Qusay" , "Al-Amarat"  ,
                "qusay" , passwordEncoder.encode("0000"));

        UserAccount userAccount1 = new UserAccount("Layana" , "Baba",
                "layana" , passwordEncoder.encode("0000"));

        userAccountService.createUserAccount(userAccount);
        userAccountService.createUserAccount(userAccount1);

    }


}

