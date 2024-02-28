package com.kob.backend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import com.kob.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public Map<String, String> register(String username, String password, String confirmedPassword) {
        Map<String,String> map =new HashMap<>();
        if(username == null){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if(password == null||confirmedPassword == null){
            map.put("error_message","密码不能为空");
            return map;
        }
        username =username.trim();
        if(username.length() == 0){
            map.put("error_message","用户名不能为空");
            return map;
        }
        if(password.length() == 0||confirmedPassword.length()==0 ){
            map.put("error_message","密码不能为空");
            return map;
        }



        if(username.length() >100){
            map.put("error_message","用户名长度不能大于100");
            return map;
        }
        if(password.length() >100||confirmedPassword.length()>100){
            map.put("error_message","密码长度不能大于100");
            return map;
        }


        if(!Objects.equals(confirmedPassword, password)){
            map.put("error_message","俩次输入的密码不一样");
            return map;
        }


        //下面要保证用户名不重复，所以要用数据库锁对比
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()){
            map.put("error_message","用户名已经存在");
            return  map;
        }


        String encodePassword =passwordEncoder.encode(password);
        String photo = "https://cdn.pixabay.com/photo/2024/02/12/16/45/rocks-8568995_1280.jpg";
        User user = new User(null,username,encodePassword,photo);
        userMapper.insert(user);
        map.put("error_message","success");
        return  map;
    }







}
