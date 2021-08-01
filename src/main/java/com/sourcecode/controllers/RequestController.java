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

    @GetMapping("/request")
    public List<Request> getAllRequests(){
        return requestService.getAllRequest();
    }

    @GetMapping("/request/{id}")
    public Request getRequest(@PathVariable Long id){
        return requestService.findRequest(id);
    }

    @DeleteMapping("/request/{id}")
    public Request deleteRequest(@PathVariable Long id){
        return requestService.deleteRequest(id);
    }

    @PutMapping("/request/{id}")
    public Request updateRequest(@PathVariable Long id, @RequestBody Request request ){
        Request updated = requestService.findRequest(id);

        updated.setContent(request.getContent());

        requestService.createRequest(updated);
        return updated;
    }

    @PostMapping("/request/likes/{reqId}")
    public Request addLike(@PathVariable Long reqId){
        Request request = requestService.findRequest(reqId);
        request.addLike();
        requestService.createRequest(request);
        return request ;
    }

}
