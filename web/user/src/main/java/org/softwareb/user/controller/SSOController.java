package org.softwareb.user.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import org.softwareb.api.user.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class SSOController {

    @Reference
    private IUserService userService;

    @RequestMapping("/hello")
    public String test(){
        String res = userService.test();
        return res;
    }
}
