package com.sourcecode;



import com.sourcecode.infrastructure.services.UserAccountService;
import com.sourcecode.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SourcecodeApplication  {


    public static void main(String[] args) {
        SpringApplication.run(SourcecodeApplication.class, args);
    }


//    private final UserAccountService userAccountService ;
//    @Autowired
//    private BCryptPasswordEncoder passwordEncoder ;
//
//    SourcecodeApplication(UserAccountService userAccountService) {
//        this.userAccountService = userAccountService;
//    }
//
//    @Override
//    public void run(String... args) throws Exception {
//        UserAccount userAccount = new UserAccount("qusay" , passwordEncoder.encode("0000")  ,
//                "Qusay" , "Al-Amarat");
//
//        UserAccount userAccount1 = new UserAccount("Layana" , passwordEncoder.encode("0000"),
//                "Layana" , "Baba");
//
//        userAccountService.createUserAccount(userAccount);
//        userAccountService.createUserAccount(userAccount1);
//
//    }


}

