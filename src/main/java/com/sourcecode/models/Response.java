package com.sourcecode.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","request","userAccount"})
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "text")
    private String content;

    private String createdAt ;

    @JsonIgnore
    @ManyToOne( fetch = FetchType.LAZY )
    private UserAccount userAccount;

    @JsonIgnore
    @ManyToOne( fetch = FetchType.LAZY)
    private Request request;

    private Integer likesCounter = 0;


    public Response(){}

    public Response(String content) {
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Request getRequest() {
        return request;
    }

    public Integer getLikesCounter() {
        return likesCounter;
    }

    public UserAccount getUserAccount() { return userAccount; }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer addLike(){
        this.likesCounter++ ;
        return this.likesCounter;
    }

    public Integer dislike(){
        if( this.likesCounter == 0)
            return this.likesCounter ;
        else
            this.likesCounter-- ;

        return this.likesCounter;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
