package com.sourcecode.models;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.annotation.ObjectIdGenerators ;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","requests","responses" , "followers" , "following"})
public class UserAccount implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String firstName ;
    private String lastName ;

    @Column(unique = true)
    private String username ;
    private String password ;

    private String imageUrl;

    @Column(columnDefinition = "text")
    private String bio ;
//    Date dateOfBirth;

    @OneToMany(fetch =FetchType.LAZY , mappedBy = "userAccount")
    private List<Request> requests ;

    @OneToMany( fetch = FetchType.LAZY , mappedBy = "userAccount" , orphanRemoval = true )
    private List<Response> responses ;


    @ManyToMany
    @JoinTable(
            name = "useraccount_useraccount",
            joinColumns = {@JoinColumn(name = "from_id")},
            inverseJoinColumns = {@JoinColumn(name = "to_id")}
    )
    public List<UserAccount> following;

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    public List<UserAccount> followers;

    public UserAccount(){}

    public UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserAccount(String firstName, String lastName, String username, String password) {
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

    public List<UserAccount> getFollowing() {
        return following;
    }

    public List<UserAccount> getFollowers() {
        return followers;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public List<UserAccount> addFollowing(UserAccount userAccount){
        this.following.add(userAccount);
        return this.following ;
    }

    public List<UserAccount> addFollower(UserAccount userAccount){
        this.followers.add(userAccount);
        return this.followers ;
    }

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

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setBio(String bio) {
        this.bio = bio;
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
