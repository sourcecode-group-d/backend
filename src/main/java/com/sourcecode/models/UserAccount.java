package com.sourcecode.models;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.annotation.ObjectIdGenerators ;

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","requests","responses"})
public class UserAccount implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String firstName ;
    private String lastName ;

    @Column(unique = true)
    private String username ;
    private String password ;

    @OneToMany(fetch =FetchType.LAZY , mappedBy = "userAccount")
    private List<Request> requests ;

    @OneToMany( fetch = FetchType.LAZY , mappedBy = "userAccount" , orphanRemoval = true )
    private List<Response> responses ;

    public UserAccount(){}

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserAccount(String username, String password , String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public List<Response> getResponses() { return responses; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password ;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
