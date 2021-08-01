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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@CrossOrigin(origins = "http://localhost:3000/")
@RestController
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
    public ResponseEntity<Request> createNewRequest(@RequestBody Request request){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        request.setUserAccount(userAccount);
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String createdAt = localDateTime.format(dateTimeFormatter);
        request.setCreatedAt(createdAt);
        request = requestService.createRequest(request);
        return new ResponseEntity<>(request , HttpStatus.CREATED) ;
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
    public Request deleteRequest(@PathVariable Long id){
        return requestService.deleteRequest(id);
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
    public Request addLike(@PathVariable Long reqId){
        Request request = requestService.findRequest(reqId);
        request.addLike();
        requestService.createRequest(request);
        return request ;
    }
}
