package com.tistory.aircook.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private")
public class PrivateController {

    @GetMapping("/test01")
    public String test01() {
        return "private/test01";
    }

    @GetMapping("/test02")
    public String test02() {
        return "private/test02";
    }

}
