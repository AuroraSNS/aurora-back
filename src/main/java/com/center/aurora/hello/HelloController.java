package com.center.aurora.hello;

import com.center.aurora.security.CurrentUser;
import com.center.aurora.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final HelloService helloService;

    @GetMapping("/hello")
    public String hello(@CurrentUser UserPrincipal userPrincipal) {
        String ret = "Hello Aurora3~!!!";
        try{
            ret += userPrincipal.getEmail();
        }
        catch (NullPointerException e){
            System.out.println("로그인 한 유저 없음");
        }
        return ret;
    }

    @GetMapping("/hello/dto")
    public HelloDto helloDto(){
        return helloService.findDefault();
    }

    @PostMapping("/hello/dto")
    public void putHello(){
        helloService.makeDefaultHello();
    }
}
