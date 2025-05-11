package com.scm.entities;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "user")
@Table(name = "users") // by default table name is user aur chahe toh yaha pe table name bhi change kar
                       // sakte hai
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    private String userId;
    @Column(name = "user_name", nullable = false) // name ko user_name kar diya in table
    private String name;
    @Column(unique = true, nullable = false) // email ko unique kar diya aur null nahi hona chahiye
    private String email;

    @Getter(value = AccessLevel.NONE) // getter nahi chahiye
    public String password;

    @Column(length = 1000) // about ki length 10000 kar di
    private String about;
    @Column(length = 1000) // profilePic ki length 10000 kar di
    private String profilePic;
    private String phoneNumber;

    // other information you want to store

    @Getter(value = AccessLevel.NONE) // getter nahi chahiye
    private boolean enabled = true;
    private boolean emailVerified = false;
    private boolean phoneVerified = false;

    @Enumerated(EnumType.STRING)
    // SELF,GOOGLE,FACEBOOK,GITHUB
    private Providers provider = Providers.SELF;
    private String providerUserId;

    // mapping karenge user se contact ke sath(one to many)

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    // bidirectional mapping na ban paye isliye yaha pe mapped by use kiya hai and
    // cascade use krne se user delete hoga toh uske saare contact bhi delete ho
    // jayenge
    private List<Contact> contacts = new ArrayList<>(); // means user ka object mil gya toh contact waali field se use
                                                        // saare contact mil jayenge kyuki woh list hai

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // list of roles[USER,ADMIN]
        // Collection of SimpGrantedAuthority[roles{ADMIN,USER}]
        Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        return roles;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

}
