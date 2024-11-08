package com.project.UserManagementSystem.Config;



import java.io.IOException;

import com.project.UserManagementSystem.Service.UserDetailsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsInfo userDetailsInfo;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header=request.getHeader("Authorization");
    // header==null
        String token=null;
        String username=null;

        if(header!=null && header.startsWith("Bearer ")) {
            token=header.substring(7);
            username=jwtUtil.extractusername(token);
        } // failed
        if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {

            UserDetails user=userDetailsInfo.loadUserByUsername(username);

            if(jwtUtil.validatetoken(username, token)) { // true
                UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } // failed
        filterChain.doFilter(request, response);
    }
}
