package com.dml.project.rbs.service;

import com.dml.project.rbs.entity.User;
import com.dml.project.rbs.model.request.LoginRequest;
import com.dml.project.rbs.model.response.LoginResponse;
import com.dml.project.rbs.repository.UserRepository;
import com.dml.project.rbs.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class LoginService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public LoginResponse createJwtToken(LoginRequest loginRequest) throws  Exception{
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        authenticate(email,password);

        final UserDetails userDetails = loadUserByUsername(email);

        String token = jwtUtil.generateToken(userDetails);

        User user = userRepository.findByEmail(email);

        return new LoginResponse(user,token);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);

        if(user!=null){
            return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),getAuthorities(user));
        }
        else{
            throw new UsernameNotFoundException("Email is not Valid!");
        }
    }

    private Set getAuthorities(User user){
        Set authorities = new HashSet();

        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });

        return authorities;
    }

    private void authenticate(String email,String password) throws Exception{
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        }
        catch(BadCredentialsException e){
            throw new BadCredentialsException("Bad Credentials From User!");
        }

    }
}
