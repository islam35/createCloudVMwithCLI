package com.sample.postgress.controller;

import com.sample.postgress.model.UserInfo;
import com.sample.postgress.service.EmployeeService;
import com.sample.postgress.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class LoginController {
    //@Autowired
    //UserInfo user;
    @Resource
    LoginService loginService;

    @PostMapping("/signup")
    public boolean signUp(@RequestBody UserInfo userInfo) {
        System.out.println(userInfo.toString());
        return loginService.signUp(userInfo);
    }


    @PostMapping("/login")
    public boolean login(@RequestBody UserInfo userInfo) {
        System.out.println(userInfo.toString());
        return loginService.login(userInfo);

    }
}
