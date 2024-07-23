
package com.tistory.aircook.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tistory.aircook.security.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


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
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        super(DEFAULT_ANT_PATH_REQUEST_MATCHER, authenticationManager);
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

    /**
     * 인증성공처리
     * @param request
     * @param response
     * @param chain
     * @param authResult the object returned from the <tt>attemptAuthentication</tt> method.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.debug("login successful");

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8).toString());

        // 응답할 데이터 생성
        Map<String, Object> data = new HashMap<>();
        data.put("status", 200);
        data.put("message", "Authentication success");
        data.put("result", authResult);

        // JSON 변환을 위한 ObjectMapper 사용
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(data);

        // 응답에 JSON 데이터 작성
        response.getWriter().write(jsonResponse);

    }

    /**
     * 인증실패처리
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        log.debug("login fail, message is [{}]", failed.getMessage());

        //로그인 실패시 401 응답 코드 반환
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8).toString());

        // 응답할 데이터 생성
        Map<String, Object> data = new HashMap<>();
        data.put("status", 401);
        data.put("message", "Authentication failed");
        data.put("result", failed.getMessage());

        // JSON 변환을 위한 ObjectMapper 사용
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(data);

        // 응답에 JSON 데이터 작성
        response.getWriter().write(jsonResponse);

    }
}

