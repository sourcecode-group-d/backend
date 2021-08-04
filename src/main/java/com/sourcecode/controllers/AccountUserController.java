package com.sourcecode.controllers;

import com.sourcecode.infrastructure.services.RequestService;
import com.sourcecode.infrastructure.services.UserAccountService;
import com.sourcecode.models.Request;
import com.sourcecode.infrastructure.services.UserAccountService;
import com.sourcecode.models.CodeChallenge;
import com.sourcecode.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountUserController {

    RestTemplate restTemplate = new RestTemplate();

    CodeChallenge codeChallenge;

    @Autowired
    private UserAccountService userAccountService;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RequestService requestService ;

    @GetMapping("/")
    public String getHome(Principal userDetails, Model model) {
        String[] cch = {
                "valid-braces",
                "fizz-buzz",
                "counting-sheep",
                "remove-string-spaces",
                "sort-the-odd",
                "fake-binary",
                "reverse-a-singly-linked-list",
                "find-max-tree-node",
                "stop-gninnips-my-sdrow",
                "perimeter-of-squares-in-a-rectangle",
                "fibonacci",
                "quick-n-choose-k-calculator",
                "factorial"
        };

        String api_url = "https://www.codewars.com/api/v1/code-challenges/";

        int randomChallenge = (int) Math.floor(Math.random() * (cch.length - 1 + 1) + 0);

        String url = api_url + "/" + cch[randomChallenge];

        codeChallenge = restTemplate.getForObject(url, CodeChallenge.class);
        System.out.println(codeChallenge);
        if (userDetails != null) {
            UserAccount user = userAccountService.findUserAccount(userDetails.getName());
            List<Request> req = requestService.findAllByMostLikes();

            model.addAttribute("allReq", req);
            model.addAttribute("user" , user);
            model.addAttribute("api",codeChallenge);
           
            return "homepage";
        } else {
            return "index";
        }
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/signup")
    public String getSignup() {
        return "signup";
    }

    @GetMapping("/homepage")
    public String getProfile() {
        return "homepage";
    }


    /**
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param dateOfBirth
     * @param bio
     * @return to create a UserAccount and save it in the DB
     */
    @PostMapping("/signup")
    public RedirectView createUserAccount(String username,
                                          String password,
                                          String firstName,
                                          String lastName,
                                          String dateOfBirth,
                                          String bio) {


        UserAccount userAccount = new UserAccount(firstName, lastName, username, passwordEncoder.encode(password));
        userAccount.setDataOfBirth(dateOfBirth);
        userAccount.setBio(bio);
        userAccount = userAccountService.createUserAccount(userAccount);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userAccount, null, new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new RedirectView("/");
    }

    /**
     * it will return the UserAccount object that is currently logged in
     *
     * @return
     */
    //    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/profile")
    public String getUserAccount(Model model) throws UnsupportedEncodingException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        if (userAccount.getProfileImage() != null)
        {
            model.addAttribute("showProfileImage" , true);
            byte[] encodeProfileImage = java.util.Base64.getEncoder().encode(userAccount.getProfileImage());
            model.addAttribute("profileImage", new String(encodeProfileImage, "UTF-8"));
        }

        if (userAccount.getCoverImage() != null)
        {
            model.addAttribute("showCoverImage" , true);
            byte[] encodeCoverImage = java.util.Base64.getEncoder().encode(userAccount.getCoverImage());
            model.addAttribute("coverImage", new String(encodeCoverImage, "UTF-8"));
        }
        model.addAttribute("user", userAccount);
        model.addAttribute("followingNum", userAccount.getFollowing().size());
        model.addAttribute("followerNum", userAccount.getFollowers().size());
        model.addAttribute("deleteAccount", true);
        model.addAttribute("showFollow", true);
        return "profile";
    }

    /**
     * @param id this param should be the the useraccount currently logged in want to follow
     * @return it will return a list of the following of the useraccount that currently logged in
     */
    @PostMapping("/following/{id}")
    public RedirectView addFollowing(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(id);
        UserAccount userAccountLoggedIn = userAccountService.findUserAccount(userDetails.getUsername());
        userAccountLoggedIn.addFollowing(userAccount);
        userAccountService.createUserAccount(userAccountLoggedIn);

        return new RedirectView("/user/{id}");
    }

    @PostMapping("/unfollow/{id}")
    public RedirectView removeFollowing(@PathVariable Long id) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(id);
        UserAccount userAccountLoggedIn = userAccountService.findUserAccount(userDetails.getUsername());
        userAccountLoggedIn.deleteFollowing(userAccount);
        userAccountService.createUserAccount(userAccountLoggedIn);

        return new RedirectView("/user/{id}");
    }

    /**
     * @param model the model will have the followings' application users
     * @return the template page follow
     */
    @GetMapping("/following")
    public String getFollowingUsers(Model model) throws UnsupportedEncodingException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        if (userAccount.getProfileImage() != null)
        {
            model.addAttribute("showProfileImage" , true);
            byte[] encodeProfileImage = java.util.Base64.getEncoder().encode(userAccount.getProfileImage());
            model.addAttribute("profileImage", new String(encodeProfileImage, "UTF-8"));
        }

        if (userAccount.getCoverImage() != null)
        {
            model.addAttribute("showCoverImage" , true);
            byte[] encodeCoverImage = java.util.Base64.getEncoder().encode(userAccount.getCoverImage());
            model.addAttribute("coverImage", new String(encodeCoverImage, "UTF-8"));
        }
        model.addAttribute("user", userAccount);
        model.addAttribute("followingNum", userAccount.getFollowing().size());
        model.addAttribute("followerNum", userAccount.getFollowers().size());
        model.addAttribute("deleteAccount", true);
        model.addAttribute("showFollow", true);
        model.addAttribute("appUsers", userAccount.getFollowing());
        return "follow";
    }

    /**
     * @param model the model will have the followers' application users
     * @return the template page follow
     */
    @GetMapping("/followers")
    public String getFollwers(Model model) throws UnsupportedEncodingException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        if (userAccount.getProfileImage() != null)
        {
            model.addAttribute("showProfileImage" , true);
            byte[] encodeProfileImage = java.util.Base64.getEncoder().encode(userAccount.getProfileImage());
            model.addAttribute("profileImage", new String(encodeProfileImage, "UTF-8"));
        }

        if (userAccount.getCoverImage() != null)
        {
            model.addAttribute("showCoverImage" , true);
            byte[] encodeCoverImage = java.util.Base64.getEncoder().encode(userAccount.getCoverImage());
            model.addAttribute("coverImage", new String(encodeCoverImage, "UTF-8"));
        }
        model.addAttribute("user", userAccount);
        model.addAttribute("followingNum", userAccount.getFollowing().size());
        model.addAttribute("followerNum", userAccount.getFollowers().size());
        model.addAttribute("deleteAccount", true);
        model.addAttribute("showFollow", true);
        model.addAttribute("appUsers", userAccount.getFollowers());
        return "follow";
    }

    /**
     * it will return the UserAccount object that is specified by id
     *
     * @return
     */
    @GetMapping("/user/{id}")
    public String getUserInfo(@PathVariable Long id, Model model) {
        UserAccount userAccount = userAccountService.findUserAccount(id);
        model.addAttribute("appUser", userAccount);
        model.addAttribute("followingNum", userAccount.getFollowing().size());
        model.addAttribute("followerNum", userAccount.getFollowers().size());
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userAccountService.findUserAccount(userDetails.getUsername()));
        UserAccount loggedUser = userAccountService.findUserAccount(userDetails.getUsername());
        if (loggedUser.getId() != id) {
            model.addAttribute("showButton", true);
            model.addAttribute("showProfile", true);
        }
        if (userAccount.getFollowers().contains(loggedUser)) {
            model.addAttribute("showButton", false);
        }
        return "otheruser";
    }


    /**
     * The user can delete his userAccount.
     * it will return the UserAccount object that deleted.
     *
     * @return
     */
    @PostMapping("/deleteuser")
    public RedirectView deleteAccount(HttpServletRequest request, HttpServletResponse response) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.deleteUserAccount(userDetails.getUsername());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return new RedirectView("/");
    }


    @GetMapping ("/aboutus")
    public String aboutUsPage(){
        return "aboutus";
    }

    @PostMapping("/profileimage")
    public RedirectView uploadProfileImage( @RequestParam("file") MultipartFile file) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        byte[] image = file.getBytes();
        userAccount.setProfileImage(image);
        userAccountService.createUserAccount(userAccount);

        return new RedirectView("/profile");
    }

    @PostMapping("/coverimage")
    public RedirectView uploadCoverImage( @RequestParam("file") MultipartFile file) throws IOException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        byte[] image = file.getBytes();
        userAccount.setCoverImage(image);
        userAccountService.createUserAccount(userAccount);

        return new RedirectView("/profile");
    }

}
