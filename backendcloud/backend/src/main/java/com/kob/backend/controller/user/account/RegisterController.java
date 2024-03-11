package com.kob.backend.controller.user.account;

import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RegisterController  {
    @Autowired
    private RegisterService registerService;


    @PostMapping("/user/account/register/")
    public Map<String,String>register(@RequestParam Map<String,String> map1){
        String username =map1.get("username");
        String password =map1.get("password");
        String confirmedPassword =map1.get("confirmedPassword");
        return registerService.register(username,password,confirmedPassword);
    }//map是一个工具 最基本的用途是存储键值对数据。你可以使用键来检索与之相关联的值
    //@RequestParam 注解将多个请求参数绑定到一个 map1 中。这样可以方便地处理多个查询参数或表单参数


} 
