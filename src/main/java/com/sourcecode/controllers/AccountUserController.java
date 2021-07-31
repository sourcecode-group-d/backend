package com.sourcecode.controllers;

import com.sourcecode.infrastructure.services.UserAccountService;
import com.sourcecode.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class AccountUserController {

    @Autowired
    private UserAccountService userAccountService ;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;

    /**
     *
     * @param userAccount
     * to create a UserAccount and save it in the DB
     * @return
     */
    @PostMapping("/signup")
    public UserAccount createUserAccount(@RequestBody UserAccount userAccount){
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        userAccount = userAccountService.createUserAccount(userAccount);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount , null , new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userAccount ;
    }

    /**
     * it will return the UserAccount object that is currently logged in
     * @return
     */
    @GetMapping("/profile")
    public ResponseEntity<UserAccount> getUserAccount(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        return new ResponseEntity<>(userAccount , HttpStatus.ACCEPTED) ;
    }

    /**
     *
     * @param id this param should be the the useraccount that the currently logged in user want to follow
     * @return  it will return a list of the following of the useraccount that currently logged in
     */
    @PostMapping("/following/{id}")
    public List<UserAccount> addFollowing(@PathVariable Long id){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(id) ;
        UserAccount userAccountLoggedIn = userAccountService.findUserAccount(userDetails.getUsername());
        userAccountLoggedIn.addFollowing(userAccount);
        userAccountService.createUserAccount(userAccountLoggedIn);

        return userAccountLoggedIn.getFollowing();
    }

    /**
     *
     * @return it will return the list for the followers of the currently logged in useraccount
     */
    @GetMapping("/followers")
    public List<UserAccount> getFollwers(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());

        return userAccount.getFollowers() ;
    }

}
