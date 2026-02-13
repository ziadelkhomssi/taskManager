package com.ilemgroup.internship.taskmanager.backend.security;

import com.ilemgroup.internship.taskmanager.backend.security.filter.NoAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.parameters.P;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Value("${allowed.origins}")
    private String[] allowedOrigins;
    @Value("${app.authentication.enabled}")
    private Boolean isAuthenticationEnabled;

    @Autowired
    private CustomOidcUserService customOidcUserService;
    @Autowired
    private NoAuthenticationFilter noAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/authentication/status"
                        ).permitAll()
                        .requestMatchers(
                                "/api/**"
                        ).authenticated()
                        .anyRequest().permitAll()
                );

        if (!isAuthenticationEnabled) {
            http.addFilterBefore(
                    noAuthenticationFilter,
                    AnonymousAuthenticationFilter.class
            );
        }
        else {
            http
                    .oauth2Login(oauth2 -> oauth2
                            .successHandler((
                                    request,
                                    response,
                                    authentication) -> {
                                String redirectUrl =
                                        request.getScheme()
                                                + "://"
                                                + request.getServerName() +
                                                "/";

                                response.sendRedirect(redirectUrl);
                            })
                            .userInfoEndpoint(userInfo -> userInfo
                                    .oidcUserService(customOidcUserService)
                            )
                    )
                    .logout(Customizer.withDefaults());
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
