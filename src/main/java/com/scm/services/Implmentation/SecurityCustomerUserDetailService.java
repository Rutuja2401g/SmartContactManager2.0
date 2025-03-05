package com.scm.services.Implmentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.scm.repositories.UserRepository;
@Service
public class SecurityCustomerUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
     
      
      
        //code to  load the user
       return userRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found with this Email"));

    }
}
