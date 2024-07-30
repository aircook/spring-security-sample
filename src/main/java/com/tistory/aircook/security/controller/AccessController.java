package com.tistory.aircook.security.controller;

import com.tistory.aircook.security.config.annoation.RequiresPermission;
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
    @RequiresPermission("access:test01:read")
    public String test01(@AuthenticationPrincipal Object object) {

        log.debug("object is [{}]", object);

        return "access/test01";
    }

    @GetMapping("/test02")
    @RequiresPermission("access:test02:read")
    public String test02(@AuthenticationPrincipal Object object) {

        log.debug("object is [{}]", object);

        return "access/test02";
    }
}
