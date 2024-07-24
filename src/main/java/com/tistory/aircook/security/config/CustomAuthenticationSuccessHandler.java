package com.tistory.aircook.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.endpoint.SecurityContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("login successful");

        log.debug("security context holder authentication name is [{}]", SecurityContextHolder.getContext().getAuthentication().getName());

        log.debug(getAllSessionInfo(request.getSession()));

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8).toString());

        // 응답할 데이터 생성
        Map<String, Object> data = new HashMap<>();
        data.put("status", 200);
        data.put("message", "Authentication success");
        data.put("result", authentication);

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
