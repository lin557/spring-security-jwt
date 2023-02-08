package com.lin.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller")
public class YourController {

    @RequestMapping("method")
    @PreAuthorize("hasAuthority('YourController:YourMethod')")
    public String yourMethod(){
        return "这个信息只有登录才能看到";
    }

}
