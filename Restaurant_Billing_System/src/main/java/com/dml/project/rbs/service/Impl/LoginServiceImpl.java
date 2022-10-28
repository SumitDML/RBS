package com.dml.project.rbs.service.Impl;

import com.dml.project.rbs.entity.UserEntity;
import com.dml.project.rbs.model.response.ResponseModel;
import com.dml.project.rbs.model.request.LoginRequest;
import com.dml.project.rbs.model.response.LoginResponse;
import com.dml.project.rbs.repository.UserRepository;
import com.dml.project.rbs.service.LoginService;
import com.dml.project.rbs.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class LoginServiceImpl implements UserDetailsService, LoginService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity createJwtToken(LoginRequest loginRequest) throws Exception {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        UserEntity users = userRepository.findByEmail(email);
        if(users == null) {
            return new ResponseEntity(new ResponseModel<>(HttpStatus.BAD_REQUEST,"Invalid Email Address!",null,null),HttpStatus.BAD_REQUEST);
        }
        else{
            String encodedPass = userRepository.getPasswordByEmail(email);
            if(!passwordEncoder.matches(password,encodedPass)){
                return new ResponseEntity(new ResponseModel<>(HttpStatus.UNAUTHORIZED,"Invalid Password!!",null,null),HttpStatus.UNAUTHORIZED);
            }
        }
        authenticate(email,password);

        final UserDetails userDetails = loadUserByUsername(email);

        String token = jwtUtil.generateToken(userDetails);

        UserEntity userEntity = userRepository.findByEmail(email);

        return new ResponseEntity(new ResponseModel<>(HttpStatus.OK, null, null, new LoginResponse("Login Successfully!",token)),HttpStatus.OK);
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity !=null){
            return new org.springframework.security.core.userdetails.User(userEntity.getEmail(), userEntity.getPassword(),getAuthorities(userEntity));
        }
        else{
            throw new UsernameNotFoundException("Email is not Valid!");
        }
    }

    private Set getAuthorities(UserEntity userEntity){
        Set authorities = new HashSet();

        userEntity.getRoleEntity().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getRoleName()));
        });

        return authorities;
    }

   private void authenticate(String email,String password) {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        }
        catch(BadCredentialsException e){

            throw new BadCredentialsException("Bad Credentials From User!");
        }
    }
}
