package com.tistory.aircook.security.controller;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access")
@Slf4j
public class AccessController {

    @GetMapping("/test01")
    public String test01(@AuthenticationPrincipal Object object) {

        log.debug("object is [{}]", object);

        return "access/test01";
    }

    @GetMapping("/test02")
    public String test02(@AuthenticationPrincipal Object object) {

        log.debug("object is [{}]", object);

        return "access/test02";
    }
}
