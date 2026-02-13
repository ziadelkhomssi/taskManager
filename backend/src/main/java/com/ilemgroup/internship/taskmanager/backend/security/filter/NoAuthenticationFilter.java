package com.ilemgroup.internship.taskmanager.backend.security.filter;

import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class NoAuthenticationFilter extends OncePerRequestFilter {
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

        final String USER_ID = "def456";
        final Optional<User> user = userRepository.findById(USER_ID);
        String role = "ADMIN";
        if (user.isPresent()) {
            role = user.get().getRole();
        }

        OidcIdToken idToken = OidcIdToken.withTokenValue("dummy")
                .claim("sub", "my-totally-unique-sub")
                .claim("oid", USER_ID)
                .build();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(
                "ROLE_" + role.toUpperCase()
        );
        OidcUser oidcUser = new DefaultOidcUser(authorities, idToken);
        OAuth2AuthenticationToken newAuthentication = new OAuth2AuthenticationToken(
                oidcUser,
                authorities,
                "oidc-client"
        );
        SecurityContextHolder.getContext().setAuthentication(newAuthentication);

        filterChain.doFilter(request, response);
    }
}