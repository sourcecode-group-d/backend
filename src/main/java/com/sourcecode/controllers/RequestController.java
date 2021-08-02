package com.sourcecode.controllers;

import com.sourcecode.infrastructure.services.RequestService;
import com.sourcecode.infrastructure.services.UserAccountService;
import com.sourcecode.models.Request;
import com.sourcecode.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@CrossOrigin(origins = "http://localhost:3000/")
//@RestController
@Controller
public class RequestController {

    @Autowired
    private RequestService requestService ;
    @Autowired
    private UserAccountService userAccountService ;

    /**
     * to save a request in the DB
     * @param request the request object that should be saved  in DB
     * @return it will return the request that have been saved
     */
    @PostMapping("/request")
    public RedirectView createNewRequest(String type, String content){
        Request request = new Request(type , content);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
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
        request.addLike();
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
        request.dislike();
        request = requestService.createRequest(request);
        return new RedirectView("/") ;
    }

    /**
     * following feeds
     * @return list of requests for the logged in user's requests
     */
//    @GetMapping("/feeds")
//            public List<Request> followingsRequests (){
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UserAccount user = userAccountService.findUserAccount(userDetails.getUsername());
//       List <UserAccount> followingAcc = user.getFollowing();
//       List<Request> followingReq = new ArrayList<>();
//       for ( UserAccount followingPerson : followingAcc)
//       {
//          for (Request oneRequest : followingPerson.getRequests())
//          {
//              followingReq.add(oneRequest);
//          }
//       }
//        return  followingReq;
//    }

    /**
     *
     * @param reqId of the request that you want to dislike it
     * @return the request object
     */
//    @PostMapping("/request/dislikes/{reqId}")
//    public RedirectView disLike(@PathVariable Long reqId){
//        Request request = requestService.findRequest(reqId);
//        request.dislike();
//        request = requestService.createRequest(request);
//        return new RedirectView("/") ;
//    }

    /**
     * following feeds
     * @return list of requests for the logged in user's requests
     */
    @GetMapping("/feeds")
            public List<Request> followingsRequests (){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount user = userAccountService.findUserAccount(userDetails.getUsername());
       Set<UserAccount> followingAcc = user.getFollowing();
       List<Request> followingReq = new ArrayList<>();
       for ( UserAccount followingPerson : followingAcc)
       {
          for (Request oneRequest : followingPerson.getRequests())
          {
              followingReq.add(oneRequest);
          }
       }
        return  followingReq;
    }

}
