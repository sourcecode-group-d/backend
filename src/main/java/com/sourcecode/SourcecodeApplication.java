package com.sourcecode;

import com.sourcecode.model.Account;
import com.sourcecode.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SourcecodeApplication  implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SourcecodeApplication.class, args);
    }

    private final AccountRepository accountRepository;

    SourcecodeApplication(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @Override
    public void run(String... args) throws Exception {
        this.accountRepository.save(new Account(
                "mohammad",
                "mohammad",
                "mohammad",
                "mohammad",
                "mohammad"));

        this.accountRepository.save(new Account(
                "ali",
                "ali",
                "ali",
                "ali",
                "ali"));
    }
}
