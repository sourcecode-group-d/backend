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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    /**
     *
     * @param response the response that you want to save
     * @param reqId the id of the request object that you want to save the response inside it
     * @return the saved response
     */
    @PostMapping("/response/{reqId}")
    public Response addNewResponse(@RequestBody Response response, @PathVariable Long reqId){
        Request request = requestService.findRequest(reqId);

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        response.setUserAccount(userAccountService.findUserAccount(userDetails.getUsername()));

        response.setRequest(request);

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String createdAt = localDateTime.format(dateTimeFormatter);
        response.setCreatedAt(createdAt);
        response = responseService.createResponse(response);

        return response;
    }

    /**
     *
     * @param reqId of the request object that you want to get all the responses inside it
     * @return a list of the responses that have beed saved inside that request
     */
    @GetMapping("/response/request/{reqId}")
    public List<Response> getAllResponsesForSpecificReq(@PathVariable Long reqId){
        Request request = requestService.findRequest(reqId);

        return request.getResponses();
    }

    /**
     *
     * @param id of the response you want to get
     * @return that response
     */
    @GetMapping("/response/{id}")
    public Response getResponse(@PathVariable Long id){
        return responseService.findResponse(id);
    }

    /**
     *
     * @param id of the response you want to delete
     * @return the deleted response
     */
    @DeleteMapping("/response/{id}")
    public Response deleteResponse(@PathVariable Long id){
        return responseService.deleteResponse(id);
    }

    /**
     *
     * @param id of the response that you want to update
     * @param response it should hold the new data
     * @return the updated response object
     */
    @PutMapping("/response/{id}")
    public Response updateResponse (@PathVariable Long id , @RequestBody Response response){
        Response updated = responseService.findResponse(id);

        updated.setContent(response.getContent());

        responseService.createResponse(updated);

        return updated;
    }

    /**
     *
     * @param id of the response that you want to add like for it
     * @return the response object
     */
    @PostMapping("/response/likes/{id}")
    public Response addLike(@PathVariable Long id){
        Response response = responseService.findResponse(id);
        response.addLike();
        return responseService.createResponse(response);
    }
}
