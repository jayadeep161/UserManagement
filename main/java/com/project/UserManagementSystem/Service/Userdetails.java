package com.project.UserManagementSystem.Service;

import com.project.UserManagementSystem.Entity.OurUsers;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class Userdetails implements UserDetails {



    private OurUsers users;//object
    private String user_email;//email
    private String password;//password

    public Userdetails(OurUsers users) {
        this.users = users;
        this.user_email = users.getEmail();
        this.password = users.getPassword();
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(users.getRole()));
    }

    @Override
    public String getPassword() {
        return  this.password;
    }

    @Override
    public String getUsername() {
        return this.user_email;
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
