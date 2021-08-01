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
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
//@CrossOrigin(origins = "http://localhost:3000/")
//@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class AccountUserController {

    @Autowired
    private UserAccountService userAccountService ;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;

    @GetMapping("/")
    public String slash(){
        return "Hello :D";
    }

//    @PostMapping("/signup")
//    public ResponseEntity<UserAccount> createUserAccount(String username,
//                                                       String password,
//                                                       String firstName,
//                                                       String lastName,
//                                                       Date dateOfBirth,
//                                                       String bio){
//
//
//        UserAccount userAccount = new UserAccount(
//                username,
//                passwordEncoder.encode(password),
//                firstName,
//                lastName,
//                dateOfBirth,
//                bio
//        );
//        userAccount = userAccountService.createUserAccount(userAccount);
//        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount , null , new ArrayList<>());
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        return new ResponseEntity<>(userAccount,HttpStatus.ACCEPTED) ;
//    }


    @RequestMapping("/signup")
    public void createUserAccount( String username,
                                     String password,
                                     String firstName,
                                     String lastName,
                                     Date dateOfBirth,
                                     String bio, HttpServletResponse response) throws IOException {


        UserAccount userAccount = new UserAccount(
                username,
                passwordEncoder.encode(password),
                firstName,
                lastName,
                dateOfBirth,
                bio
        );
        userAccount = userAccountService.createUserAccount(userAccount);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount , null , new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        response.sendRedirect("http://localhost:3000/profile");
    }
    /**
     * it will return the UserAccount object that is currently logged in
     * @return
     */
    @GetMapping("/profile")
    public List<UserAccount> getUserAccount(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        List<UserAccount> users=new ArrayList<>();
        users.add(userAccount);
        return users;
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
