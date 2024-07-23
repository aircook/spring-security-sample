package com.tistory.aircook.security.config;

import com.tistory.aircook.security.entity.UserEntity;
import com.tistory.aircook.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

//인증공급자, 필터를 통해 넘겨받은 요청정보를 가지고 인증을 처리한다.
//CustomAuthenticationFilter의 attemptAuthentication()메소드안을 보면 getAuthenticationManager().authenticate() 를 호출하고 있다.
//https://gregor77.github.io/2021/05/18/spring-security-03/
//Global AuthenticationManager configured with AuthenticationProvider bean with name customAuthenticationProvider
@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {
    
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Spring DI
     * @param userService 사용자서비스, 데이터베이스 정보
     * @param passwordEncoder 암호인코더
     */
    public CustomAuthenticationProvider(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 인증처리
     * @param authentication the authentication request object.
     * @return 인증정보, Authority가 포함되어 있다.
     * @throws AuthenticationException 인증예외
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        log.debug("username is [{}], password is [{}]", username, password);

        UserEntity user = userService.findByUserid(username);
        if (user == null) {
            log.debug("Username is not found");
            throw new BadCredentialsException("username is not found. username=" + username);
        }

        log.debug("request password is [{}], database password is [{}]", password, user.getPassword());

        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            log.debug("Password is not matched");
            throw new BadCredentialsException("password is not matched");
        }

        log.debug("Authentication is successful");

        List<GrantedAuthority> authorities = List.of((GrantedAuthority) user::getRole);

        return new CustomAuthenticationToken(username, password, authorities);
    }

    //supports() 메서드에 대한 추가 설명을 하자면 AuthenticationFilter 수준에서 아무것도 맞춤 구성하지 않으면(지금은 이런 케이스)
    // UsernamePasswordAuthenticationToken 클래스가 형식을 정의한다고 되어있다.
    @Override
    public boolean supports(Class<?> authentication) {
        //return CustomAuthenticationToken.class.isAssignableFrom(authentication);

        return authentication.equals(CustomAuthenticationToken.class);
    }
}