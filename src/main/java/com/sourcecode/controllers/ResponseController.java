package com.sourcecode.controllers;

import com.sourcecode.infrastructure.services.RequestService;
import com.sourcecode.infrastructure.services.ResponseService;
import com.sourcecode.infrastructure.services.UserAccountService;
import com.sourcecode.models.Request;
import com.sourcecode.models.Response;
import com.sourcecode.models.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000/")
//@RestController
@Controller
public class ResponseController {

    @Autowired
    private UserAccountService userAccountService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private ResponseService responseService;

    /**
     * post a comment
     * @param userId
     * @param requestId
     * @param comment
     * @param model
     * @return redirect to /comments endpoint
     */
    @PostMapping ("/response/{userId}/{requestId}")
    public RedirectView  addingCommentstoPosts(@PathVariable Long userId , @PathVariable Long requestId  ,
                                               String comment , Model model){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Request req = requestService.findRequest(requestId);

//        List <Response> res = new ArrayList<>();
        Response response1= new Response(comment);
//        res.add(response1);
        response1.setRequest(req);
//        req.setResponses(res);
        responseService.createResponse(response1);
        model.addAttribute("response1",response1);
        System.out.println(">>>>>>>>>>>>>>>>>>.Comment <<<<<<<<<<<<<<," +response1.getContent());
       return  new RedirectView("/response/{userId}/{requestId}");

    }

    /**
     * returns comment page
     * @return comment page
     */
    @GetMapping("/comments")
    public  String getComments(){

        return "comments" ;
    }

    /**
     * rendering specific post with it's comments
     * @param userId
     * @param requestId
     * @param model
     * @return html page comments
     */
    @GetMapping("/response/{userId}/{requestId}")
    public String getResAndReq (@PathVariable Long userId , @PathVariable Long requestId , Model model ){
    UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Request request = requestService.findRequest(requestId);
    Iterable<Response> res = request.getResponses();
    UserAccount userPost = userAccountService.findUserAccount(request.getUserAccount().getId());

    model.addAttribute("userPost" , userPost);
    model.addAttribute("user" ,userDetails );
    model.addAttribute("responses" , res);
    model.addAttribute("request" , request);
    return "comments";
}



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

    /**
     *
     * @param id of the response that you want to dislike it
     * @return the response object
     */
    @PostMapping("/response/dislikes/{id}")
    public Response disLike(@PathVariable Long id){
        Response response = responseService.findResponse(id);
        response.dislike();
        response = responseService.createResponse(response);
        return response ;
    }
}
