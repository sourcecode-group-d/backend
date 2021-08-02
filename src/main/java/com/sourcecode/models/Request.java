package com.sourcecode.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","userAccount"})
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String type ;

    @Column(columnDefinition = "text")
    private String content ;

    private String createdAt ;

    private Integer likesCounter = 0 ;

    @OneToMany( mappedBy = "reqVotes" , fetch = FetchType.LAZY , orphanRemoval = true)
    private Set<UserAccount> voters = new HashSet<>();

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

    public Set<UserAccount> getVoters() {
        return voters;
    }


    public Integer addLike(){
        this.likesCounter++ ;
        return this.likesCounter;
    }

    public Integer dislike(UserAccount userAccount){
        this.voters.remove(userAccount);
        this.likesCounter=voters.size();
        return this.likesCounter;
    }

    public Integer getLikesCounter() {
        return likesCounter;
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


    public void setLikesCounter(Integer likesCounter) {
        this.likesCounter = likesCounter;
    }

}
