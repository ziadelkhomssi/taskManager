package com.ilemgroup.internship.taskmanager.backend.security.filter;

import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import com.ilemgroup.internship.taskmanager.backend.security.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

/*

REMOVED WHEN REAL AUTHENTICATION IS IMPLEMENTED!!

*/

@Component
public class TemporaryAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String USER_ID = "abc123";
        final Optional<User> user = userRepository.findById(USER_ID);
        String role = "ADMIN";
        if (user.isPresent()) {
            role = user.get().getRole();
        }

        CustomUserDetails userDetails = new CustomUserDetails(USER_ID, role);
        Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        filterChain.doFilter(request, response);
    }
}
