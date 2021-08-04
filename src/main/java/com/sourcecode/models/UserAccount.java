package com.sourcecode.models;

import com.fasterxml.jackson.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;

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

    private String imageUrl ;

    private String dataOfBirth ;

    @Column(columnDefinition = "text")
    private String bio ;

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
    public Set<UserAccount> following= new HashSet<>();

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    public List<UserAccount> followers;


    @ManyToOne
    private Request reqVotes;

    @Basic(fetch=FetchType.EAGER) @Lob
    @Column(name = "profile_image")
    private byte[] profileImage;

    @Basic(fetch=FetchType.EAGER) @Lob
    @Column(name = "cover_image")
    private byte[] coverImage;

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

    public UserAccount(String firstName, String lastName, String username, String password, String bio) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.bio = bio;
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

    public Set<UserAccount> getFollowing() {
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

    public String getDataOfBirth() {
        return dataOfBirth;
    }

    public Request getReqVotes() {
        return reqVotes;
    }

    public Set<UserAccount> addFollowing(UserAccount userAccount){
        this.following.add(userAccount);
        return this.following ;
    }

    public Set<UserAccount> deleteFollowing(UserAccount userAccount){
        this.following.remove(userAccount);
        return this.following ;
    }

    public List<UserAccount> addFollower(UserAccount userAccount){
        this.followers.add(userAccount);
        return this.followers ;
    }

    public void setReqVotes(Request reqVotes) {
        this.reqVotes = reqVotes;
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

    public void setDataOfBirth(String dataOfBirth) {
        this.dataOfBirth = dataOfBirth;
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

    public byte[] getProfileImage() {
        return profileImage;
    }

    public byte[] getCoverImage() {
        return coverImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }

    public void setCoverImage(byte[] coverImage) {
        this.coverImage = coverImage;
    }


}
