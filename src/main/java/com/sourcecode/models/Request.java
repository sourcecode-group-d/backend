package com.sourcecode.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","responses","userAccount"})
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String type ;

    @Column(columnDefinition = "text")
    private String content ;

    private String createdAt ;

    private Integer LikesCounter ;

    @ManyToOne( fetch = FetchType.LAZY )
    private UserAccount userAccount ;

    @OneToMany( mappedBy = "request" , fetch = FetchType.LAZY , orphanRemoval = true)
    private List<Response> responses ;


    public Request(){}

    public Request(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public Long getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public List<Response> getResponses() { return responses; }

    public void setType(String type) {
        this.type = type;
    }


    public Integer addLike(){
        if(this.LikesCounter == null) this.LikesCounter = 0 ;
        this.LikesCounter++ ;
        return this.LikesCounter;
    }



    public Integer getLikesCounter() {
        return LikesCounter;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public void setResponses(List<Response> responses) { this.responses = responses; }
}
