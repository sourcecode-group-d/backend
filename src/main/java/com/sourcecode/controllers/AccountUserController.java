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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

//@RestController
@Controller
public class AccountUserController {

    @Autowired
    private UserAccountService userAccountService ;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder ;

    @GetMapping("/")
    public String getHome(Principal userDetails , Model model ){

        if (userDetails != null) {
            UserAccount user = userAccountService.findUserAccount(userDetails.getName());
            model.addAttribute("user" , user);
            return "homepage";
        }
        else {
            return "index";
        }
    }

    @GetMapping("/login")
    public String getLogin(){
        return "login";
    }

    @GetMapping("/signup")
    public String getSignup(){
        return "signup";
    }

    @GetMapping("/homepage")
    public String getProfile(){
        return "homepage";
    }



     /**
     *
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param bio
     * @return
     * to create a UserAccount and save it in the DB
     */
    @PostMapping("/signup")
    public RedirectView createUserAccount(String username,
                                          String password,
                                          String firstName,
                                          String lastName,
                                          String dateOfBirth,
                                          String bio){


        UserAccount userAccount = new UserAccount(firstName, lastName, username, passwordEncoder.encode(password));
        userAccount.setDataOfBirth(dateOfBirth);
        userAccount.setBio(bio);
        userAccount = userAccountService.createUserAccount(userAccount);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount , null , new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/") ;
    }

    /**
     * it will return the UserAccount object that is currently logged in
     * @return
     */
    @GetMapping("/profile")
//    @CrossOrigin(origins = "http://localhost:3000")
    public String getUserAccount(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        return "homepage";
    }

    /**
     *
     * @param id this param should be the the useraccount currently logged in want to follow
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
        SecurityContextHolder.getContext().getAuthentication();
        return userAccount.getFollowers() ;
    }

    /**
     * it will return the UserAccount object that is specified by id
     * @return
     */
    @GetMapping("/user/{id}")
    public UserAccount getUserInfo(@PathVariable Long id){
        UserAccount userAccount = userAccountService.findUserAccount(id);
        return userAccount;
    }


    /**
     * The user can delete his userAccount.
     * it will return the UserAccount object that deleted.
     * @return
     */
    @DeleteMapping("/user")
    public UserAccount deleteAccount(HttpServletRequest request, HttpServletResponse response){
        UserDetails userDetails = (UserDetails)  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.deleteUserAccount(userDetails.getUsername());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return userAccount;
    }

}
