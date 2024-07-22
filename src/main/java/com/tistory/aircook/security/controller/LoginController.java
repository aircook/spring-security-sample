package com.tistory.aircook.security.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    /**
     * uri redirected after authenticating successfully
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/success")
    public Map<String, String> success(@AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Username is [{}]", userDetails.getUsername());
        log.debug("Password is [{}]", userDetails.getPassword());
        log.debug("Authorities is [{}]", userDetails.getAuthorities());

        return Map.of("username", userDetails.getUsername(), "authorities", userDetails.getAuthorities().toString());

    }

    @PostMapping("/failure")
    public String failure() {
        return "로그인 실패하였습니다.";
    }
}
