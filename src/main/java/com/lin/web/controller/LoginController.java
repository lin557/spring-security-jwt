package com.lin.web.controller;

import com.lin.util.SpringSecurityUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @RequestMapping("login")
    public Object login(){
        // 用户名，这个从你的登录表单拿
        String username="admin";
        // 密码，这个从你的登录表单拿
        String password="password";
        // 权限字符串，这个从你的数据库里读
        List<String> authorities=new ArrayList<>();
        authorities.add("YourController:YourMethod");

        // 判断是否登陆
        if(SpringSecurityUtil.isLogin()){
            // 登陆了打印一下当前用户名
            System.out.println(SpringSecurityUtil.getCurrentUsername());
        }else{
            // 没登录则进行一次登录
            SpringSecurityUtil.login(username,password,authorities);
        }
        return "success";
    }

}
