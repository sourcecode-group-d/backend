package com.sourcecode.controllers;

import com.sourcecode.infrastructure.services.RequestService;
import com.sourcecode.infrastructure.services.UserAccountService;
import com.sourcecode.models.Request;
import com.sourcecode.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@CrossOrigin(origins = "http://localhost:3000/")
@Controller
public class RequestController {

    @Autowired
    private RequestService requestService ;
    @Autowired
    private UserAccountService userAccountService ;


    @PostMapping("/request")
    public RedirectView createNewRequest(String type, String content){
        Request request = new Request(type , content);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        Iterable req = userAccount.getRequests();
        request.setUserAccount(userAccount);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String createdAt = localDateTime.format(dateTimeFormatter);
        request.setCreatedAt(createdAt);
        request = requestService.createRequest(request);
        return new RedirectView("/");
    }

    /**
     *
     * @return it will return the all requests in the DB
     */
    @GetMapping("/request")
    public List<Request> getAllRequests(){

        return requestService.getAllRequest();
    }

    /**
     *
     * @param id of the request that you want
     * @return it will return this request object
     */
    @GetMapping("/request/{id}")
    public Request getRequest(@PathVariable Long id){
        return requestService.findRequest(id);
    }

    /**
     *
     * @param id of the request that should be deleted
     * @return the request that have been deleted
     */
    @DeleteMapping("/request/{id}")
    public RedirectView deleteRequest(@PathVariable Long id){
        requestService.deleteRequest(id);
        return new RedirectView("/") ;
    }

    /**
     *
     * @param id of the request that you want to update
     * @param request it should hold the new data
     * @return the updated request object
     */
    @PutMapping("/request/{id}")
    public Request updateRequest(@PathVariable Long id, @RequestBody Request request ){
        Request updated = requestService.findRequest(id);
        updated.setContent(request.getContent());
        requestService.createRequest(updated);
        return updated;
    }

    /**
     *
     * @param reqId of the request that you want to add like for it
     * @return the request object
     */
    @PostMapping("/request/likes/{reqId}")
    public RedirectView addVote(@PathVariable Long reqId){
        Request request = requestService.findRequest(reqId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        userAccount.setReqVotes(request);
        userAccountService.createUserAccount(userAccount);
        request.setLikesCounter(request.getVoters().size());
        requestService.createRequest(request);
        return new RedirectView("/");
    }

    /**
     *
     * @param reqId of the request that you want to dislike it
     * @return the request object
     */
    @PostMapping("/request/dislikes/{reqId}")
    public RedirectView disVote(@PathVariable Long reqId){
        Request request = requestService.findRequest(reqId);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        userAccountService.createUserAccount(userAccount);
        request.dislike(userAccount);
        requestService.createRequest(request);
        return new RedirectView("/") ;
    }

    /**
     * following feeds
     * @return list of requests for the logged in user's requests
     */
//    @GetMapping("/feeds")
//      public String followingsRequests (Model model){
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UserAccount user = userAccountService.findUserAccount(userDetails.getUsername());
//       Set<UserAccount> followingAcc = user.getFollowing();
//       List<Request> followingReq = new ArrayList<>();
//       for ( UserAccount followingPerson : followingAcc)
//       {
//          for (Request oneRequest : followingPerson.getRequests())
//          {
//              followingReq.add(oneRequest);
//          }
//       }
//       model.addAttribute("user",user);
//
//       return "feeds";
//    }

    @GetMapping("/feeds")
    public String getFeeds(Principal principal, Model model){

        UserAccount user = userAccountService.findUserAccount(principal.getName());
        model.addAttribute("user",user);
        return "feeds";
    }


    @GetMapping("/qa")
    public String getAllRequests2(Principal principal , Model m) {

        UserAccount user = userAccountService.findUserAccount(principal.getName());
        List<UserAccount> allUsers= new ArrayList<>(userAccountService.findAllUsers());
        List<Request> req = requestService.findAllByMostLikes();
        m.addAttribute("reqFiltered" , req);
        allUsers.remove(user);
        m.addAttribute("allUsers" , allUsers);
        m.addAttribute("user", user );
        return  "allFeeds";
    }


}
