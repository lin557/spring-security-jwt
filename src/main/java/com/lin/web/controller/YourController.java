package com.lin.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class YourController {

    @RequestMapping("/api/logout")
    @PreAuthorize("hasAuthority('YourController:YourMethod')")
    public String yourMethod(){
        log.info("成功访问");
        return "这个信息只有登录才能看到";
    }

}
