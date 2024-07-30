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

        return new AuthorizationDecision(isAccessAllowed(authentication.get(), requestAuthorizationContext.getRequest().getRequestURI()));
    }

    private boolean isAccessAllowed(Authentication authentication, String requestUrl) {
        // 여기에 실제 권한 확인 로직을 구현합니다
        // 예: 데이터베이스 조회, 외부 서비스 호출 등
        //테스트를 위해 test01만 true
        if (requestUrl.equals("/access/test01")) {
            return true;
        } else {
            return false;
        }
    }
}

