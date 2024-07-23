package com.tistory.aircook.security.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//https://gregor77.github.io/2021/05/18/spring-security-03/
public class CustomAuthenticationToken extends AbstractAuthenticationToken {
    private String email;
    private String credentials;

    public CustomAuthenticationToken(String email, String credentials) {
        super(null);
        this.email = email;
        this.credentials = credentials;
        super.setAuthenticated(false);
    }

    public CustomAuthenticationToken(String email, String credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.email = email;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public CustomAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
    }

    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    @Override
    public Object getPrincipal() {
        return this.email;
    }
}
