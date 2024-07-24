package com.tistory.aircook.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.debug("login fail, message is [{}]", exception.getMessage());
        log.debug(getAllSessionInfo(request.getSession()));

        //로그인 실패시 401 응답 코드 반환
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8).toString());

        // 응답할 데이터 생성
        Map<String, Object> data = new HashMap<>();
        data.put("status", 401);
        data.put("message", "Authentication failed");
        data.put("result", exception.getMessage());

        // JSON 변환을 위한 ObjectMapper 사용
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(data);

        // 응답에 JSON 데이터 작성
        response.getWriter().write(jsonResponse);
    }

    private String getAllSessionInfo(HttpSession session) {
        StringBuilder sessionInfo = new StringBuilder();
        Enumeration<String> attributeNames = session.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            Object attributeValue = session.getAttribute(attributeName);
            sessionInfo.append(attributeName).append(": ").append(attributeValue).append("\n");
        }

        return !sessionInfo.isEmpty() ? sessionInfo.toString() : "세션에 저장된 정보가 없습니다.";
    }
}
