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

import java.util.List;
import java.util.Set;

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
        request = requestService.createRequest(request);
        return new ResponseEntity<>(request , HttpStatus.CREATED) ;
    }

    @GetMapping("/request")
    public List<Request> getAllRequests(){
        return requestService.getAllRequest();
    }

    @DeleteMapping("/request/{id}")
    public Request deleteRequest(@PathVariable Long id){
        return requestService.deleteRequest(id);
    }

}
