package com.freitagfelipe.todoapp.services.authorization;

import com.freitagfelipe.todoapp.repositories.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = this.repository.findByUsername(username);

        if (userDetails == null) {
            throw new UsernameNotFoundException("Username not found");
        }

        return userDetails;
    }

}
