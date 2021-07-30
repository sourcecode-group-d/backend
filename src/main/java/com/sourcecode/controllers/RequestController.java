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
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000/")
@Controller
public class RequestController {

    @Autowired
    private RequestService requestService ;
    @Autowired
    private UserAccountService userAccountService ;

    @PostMapping("/newrequest")
    public ResponseEntity<Request> createNewRequest(@RequestBody Request request1){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserAccount userAccount = userAccountService.findUserAccount(userDetails.getUsername());
        request1.setUserAccount(userAccount);
        request1 = requestService.createRequest(request1);
        return new ResponseEntity<>(request1 , HttpStatus.CREATED);
    }



}
