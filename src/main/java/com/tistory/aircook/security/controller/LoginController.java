package com.tistory.aircook.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

    @GetMapping("/success")
    public Map<String, String> success(@AuthenticationPrincipal UserDetails userDetails){

        log.debug("Username is [{}]", userDetails.getUsername());
        log.debug("Password is [{}]", userDetails.getPassword());
        log.debug("Authorities is [{}]", userDetails.getAuthorities());

        return Map.of("username", userDetails.getUsername(), "authorities", userDetails.getAuthorities().toString());

    }

}
