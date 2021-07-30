package com.sourcecode.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String type ;

    @Column(columnDefinition = "text")
    private String content ;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "useraccount_id")
    @JsonIgnoreProperties("request")
    private UserAccount userAccount ;

    @OneToMany( mappedBy = "request" , fetch = FetchType.LAZY)
    @JsonIgnoreProperties("request")
    private Set<Response> responses = new HashSet<>();

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

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public Set<Response> getResponses() { return responses; }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public void setResponses(Set<Response> responses) { this.responses = responses; }
}
