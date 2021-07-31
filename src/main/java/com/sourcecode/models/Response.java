package com.sourcecode.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","request","userAccount"})
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(columnDefinition = "text")
    private String content;

    @JsonIgnore
    @ManyToOne( fetch = FetchType.LAZY )
    private UserAccount userAccount;

    @JsonIgnore
    @ManyToOne( fetch = FetchType.LAZY)
    private Request request;


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
