package com.kob.backend.service.impl.user;

import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.InfoService;
import com.mysql.cj.util.DnsSrv;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service//每次写这个api都要加注解
public class InfoServiceImpl implements InfoService {
//实现方法
    @Override
    public Map<String, String> getinfo() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();//得到用户的上下文信息
        UserDetailsImpl loginUser= (UserDetailsImpl) authentication.getPrincipal();
        User user =loginUser.getUser();

        Map<String,String> map  =new HashMap<>();
        map.put("error_message","success");
        map.put("id",user.getId().toString());
        map.put("username", user.getUsername());
        map.put("photo", user.getPhoto());


        return map;
    }
}
