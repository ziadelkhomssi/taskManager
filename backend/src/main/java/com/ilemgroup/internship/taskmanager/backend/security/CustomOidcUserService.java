package com.ilemgroup.internship.taskmanager.backend.security;

import com.ilemgroup.internship.taskmanager.backend.entity.User;
import com.ilemgroup.internship.taskmanager.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class CustomOidcUserService implements OAuth2UserService<OidcUserRequest, OidcUser> {

    @Autowired
    private UserRepository userRepository;

    private final OAuth2UserService<OidcUserRequest, OidcUser> delegate = new OidcUserService();

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = delegate.loadUser(userRequest);

        String oid = oidcUser.getIdToken().getClaims().get("oid").toString();

        User user = userRepository.findById(oid)
                .orElseThrow(() -> new OAuth2AuthenticationException("User not found with OID: " + oid));

        Collection<GrantedAuthority> mappedAuthorities = new HashSet<>(oidcUser.getAuthorities());
        mappedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));

        return new DefaultOidcUser(mappedAuthorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
