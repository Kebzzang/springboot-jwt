package com.keb.jwt.config.auth;


import com.keb.jwt.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.keb.jwt.repository.UserRepository;
//localhost:8080/login -> 동작 x disable() 처리했음



@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Principal Details Service load User By Username");
        User userEntity=userRepository.findByUsername(username);

        return new PrincipalDetails(userEntity);
    }
}
