package com.sourcecode.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "text")
    private String content;

    @ManyToOne( fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    @JsonIgnoreProperties("response")
    private Request request;

    @ManyToOne( fetch = FetchType.LAZY )
    @JoinColumn(name = "test_id")
    @JsonIgnoreProperties("response")
    private UserAccount userAccount;

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

    public Request getRequest() {
        return request;
    }

    public UserAccount getUserAccount() { return userAccount; }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
}
