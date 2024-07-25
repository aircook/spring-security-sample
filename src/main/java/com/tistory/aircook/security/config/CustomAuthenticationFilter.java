
package com.tistory.aircook.security.config;

import com.tistory.aircook.security.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;


//https://gregor77.github.io/2021/05/18/spring-security-03/
//Custom 로그인 필터, Bean이 아니다.. AbstractAuthenticationProcessingFilter -> Http-Browser-Base의 인증의 핵심 클래스
//로그인 처리경로를 /api/v1/login 로 지정
//로그인처리, 로그인성공처리, 로그인실패처리
@Slf4j
public class CustomAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final AntPathRequestMatcher DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/api/v1/login",
            "POST");

    private final AuthenticationManager authenticationManager;

    private final JWTUtil jwtUtil;

    //생성자, 인증관리자(관리자는 인증공급자랑 연결), JWT Token발행을 위해 클래스 가져옴
    //시큐리티 설정에 필터 추가됨 http.addFilterAfter(new CustomAuthenticationFilter(authenticationManager, jwtUtil), CsrfFilter.class);
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager,
                                      JWTUtil jwtUtil,
                                      DelegatingSecurityContextRepository delegatingSecurityContextRepository) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
        this.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        this.setAuthenticationFailureHandler(new CustomAuthenticationFailureHandler());
        this.setSecurityContextRepository(delegatingSecurityContextRepository);
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 인증처리, 인증공급자에게 위임
     * @param request from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a redirect as part of a multi-stage authentication process (such as OIDC).
     * @return 인증정보
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.debug("authenticationManager is [{}]", authenticationManager);
        log.debug("jwtUtil is [{}]", jwtUtil);

        String email = request.getParameter("username");
        String credentials = request.getParameter("password");

        log.debug("email is [{}], credentials is [{}]", email, credentials);

        return getAuthenticationManager().authenticate(new CustomAuthenticationToken(email, credentials));
    }

}

