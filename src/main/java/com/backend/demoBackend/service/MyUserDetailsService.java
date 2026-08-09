package com.backend.demoBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.demoBackend.model.UserPrincipal;
import com.backend.demoBackend.model.User.User;
import com.backend.demoBackend.repository.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserById(username);

        if (user == null) {
            System.out.println("User Name Not Found");
            throw new UsernameNotFoundException("User Name Not Found");
        }

        return new UserPrincipal(user);
    }

}
