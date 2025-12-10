package com.example.auth_service.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface IUserDetailsService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
