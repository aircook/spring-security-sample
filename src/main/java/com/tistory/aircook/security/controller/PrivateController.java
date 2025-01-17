package com.tistory.aircook.security.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Enumeration;

@RestController
@RequestMapping("/private")
@Slf4j
public class PrivateController {

    @GetMapping("/test01")
    public String test01(@AuthenticationPrincipal Object object, HttpSession httpSession) {

        log.debug("object is [{}]", object);
        log.debug(getAllSessionInfo(httpSession));

        return "private/test01";
    }

    @GetMapping("/test02")
    public String test02(@AuthenticationPrincipal Object object, HttpSession httpSession) {

        log.debug("object is [{}]", object);
        log.debug(getAllSessionInfo(httpSession));

        return "private/test02";
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
