package com.tistory.aircook.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;


import java.util.function.Supplier;

@Component
@Slf4j
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext requestAuthorizationContext) {

        log.debug("authentication name is [{}]", authentication.get().getName());
        log.debug("authentication principal is [{}]", authentication.get().getPrincipal());
        log.debug("authentication credentials is [{}]", authentication.get().getCredentials());

        log.debug("request method is [{}]", requestAuthorizationContext.getRequest().getMethod());
        log.debug("request uri is [{}]", requestAuthorizationContext.getRequest().getRequestURI());


        return new AuthorizationDecision(false);
    }
}

