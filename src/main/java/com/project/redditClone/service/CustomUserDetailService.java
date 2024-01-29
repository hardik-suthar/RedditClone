package com.project.redditClone.service;

import com.project.redditClone.dao.UserRepository;
import com.project.redditClone.entity.User;
import com.project.redditClone.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findById(username).orElse(null);
        if(user == null) throw new NotFoundException("User name '" + username + "' not found");

        return user;
    }
}
