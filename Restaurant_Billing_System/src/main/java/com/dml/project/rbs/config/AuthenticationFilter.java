package com.dml.project.rbs.config;

import com.dml.project.rbs.service.Impl.LoginServiceImpl;
import com.dml.project.rbs.util.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private LoginServiceImpl loginServiceImpl;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader("Authorization");


        String jwtToken = null;
        String email = null;
        if(header!=null && header.startsWith("Bearer")){
            jwtToken = header.substring(7);

            try{
                email = jwtUtil.extractEmailFromToken(jwtToken);

            }
            catch (IllegalArgumentException e){
                System.out.println("Unable To Find JWT Token");
            }
            catch (ExpiredJwtException e){
                System.out.println("Jwt token is Expired");
            }
        }
        else {
            System.out.println("Jwt Token does not start with Bearer");
        }

        if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = loginServiceImpl.loadUserByUsername(email);

            if(jwtUtil.validateToken(jwtToken,userDetails)){
               UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                       new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
               usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }

        }
        filterChain.doFilter(request,response);
    }
}
