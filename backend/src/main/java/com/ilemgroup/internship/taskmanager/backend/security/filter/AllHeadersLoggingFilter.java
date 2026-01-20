package com.ilemgroup.internship.taskmanager.backend.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class AllHeadersLoggingFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(AllHeadersLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        Collections.list(httpRequest.getHeaderNames())
                .forEach(headerName -> {
                    String headerValue = httpRequest.getHeader(headerName);
                    logger.info("Header: {} = {}", headerName, headerValue);
                });

        chain.doFilter(request, response);
    }
}
