package com.lin.web.controller;

import com.lin.security.jwt.JwtUtils;
import com.lin.util.SpringSecurityUtil;
import com.lin.util.SysUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LoginController {

    @RequestMapping("/auth/login")
    public Object login(){
        // 用户名，这个从你的登录表单拿
        String username="admin";
        // 密码，这个从你的登录表单拿
        String password="password";
        // 权限字符串，这个从你的数据库里读
        List<String> authorities=new ArrayList<>();
        authorities.add("YourController:YourMethod");

        // 用uuid生成
        String sessionId = SysUtils.getUuid();

        // 判断是否登陆
        if(SpringSecurityUtil.isLogin()){
            // 登陆了打印一下当前用户名
            System.out.println(SpringSecurityUtil.getCurrentUsername());
        }else{
            // 没登录则进行一次登录 用 sessionId 代替用户名 因为web/app/pad可能同时使用一个账号登录
            SpringSecurityUtil.login(sessionId, password, authorities);

        }
        // 生成token 返回给用户

        String secret = "secret";
        String accessToken = JwtUtils.createToken(sessionId, secret, "", 30);
        String refreshToken = JwtUtils.createToken(sessionId, secret, "", 300);
        HashMap<String, String> map = new HashMap<>();
        map.put("session_id", sessionId);
        map.put("access_token", accessToken);
        map.put("refresh_token", refreshToken);
        return map;
    }

}
