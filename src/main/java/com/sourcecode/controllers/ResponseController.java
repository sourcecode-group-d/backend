package com.sourcecode.controllers;

import com.sourcecode.infrastructure.services.RequestService;
import com.sourcecode.infrastructure.services.ResponseService;
import com.sourcecode.infrastructure.services.UserAccountService;
import com.sourcecode.models.Request;
import com.sourcecode.models.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
@RestController
public class ResponseController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ResponseService responseService;

    @PostMapping("/response/{id}")
    public Response addNewResponse(@RequestBody Response response, @PathVariable Long id){
        Request request = requestService.findRequest(id);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        response.setUserAccount(userAccountService.findUserAccount(userDetails.getUsername()));

        response.setRequest(request);

        response = responseService.createResponse(response);

        return response;
    }

    @GetMapping("/response/{id}")
    public List<Response> getAllResponses(@PathVariable Long id){
        Request request = requestService.findRequest(id);

        return request.getResponses();
    }

    @DeleteMapping("/response/{id}")
    public Response deleteResponse(@PathVariable Long id){
        return responseService.deleteResponse(id);
    }


}
