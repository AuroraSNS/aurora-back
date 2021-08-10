package com.center.aurora.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        log.debug("Hello API 호출됨 !!!");
        return "Hello Aurora~!!!";
    }
}
