package com.project.UserManagementSystem.Service;

import com.project.UserManagementSystem.Entity.OurUsers;
import com.project.UserManagementSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsInfo implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<OurUsers> user=userRepository.findByEmail(username);
        System.out.println(user.get().getName());

        return user.map(object-> new Userdetails(object)).orElseThrow(()-> new RuntimeException("user not found"));
    }
}
