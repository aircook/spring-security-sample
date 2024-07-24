package com.tistory.aircook.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
@Slf4j
public class PrivateController {

    @GetMapping("/test01")
    public String test01(@AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Username is [{}]", userDetails.getUsername());
        log.debug("Password is [{}]", userDetails.getPassword());
        log.debug("Authorities is [{}]", userDetails.getAuthorities());

        return "private/test01";
    }

    @GetMapping("/test02")
    public String test02(@AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Username is [{}]", userDetails.getUsername());
        log.debug("Password is [{}]", userDetails.getPassword());
        log.debug("Authorities is [{}]", userDetails.getAuthorities());

        return "private/test02";
    }

}
