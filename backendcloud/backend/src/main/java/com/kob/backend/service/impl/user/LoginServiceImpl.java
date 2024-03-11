package com.kob.backend.service.impl.user;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.account.LoginService;
import com.kob.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
@Service//这个注解是让springboot自动识别为service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;//用来验证登录
    @Override
    public Map<String, String> getToken(String username, String password) {
        //这里定义的login是在service定义的接口，即api名称，这里是login的具体实现方法
        //将用户名跟密码封装成一个类  authentication认证 Authorization授权
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username,password);


        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //用这个方法看所输入的账号密码是否一致 一个api .var 即可自动定义变量名，如果登陆失败，这个方法会自动处理

        //登陆成功后需要将用户信息取出来，下面是实现方法
        UserDetailsImpl loginUser =(UserDetailsImpl) authenticate.getPrincipal();
        User user = loginUser.getUser();

        String  jwt = JwtUtil.createJWT(user.getId().toString());

        //返回结果
        Map<String,String>map=new HashMap<>();
        map.put("error_message","success");
        map.put("token",jwt);



        return map;
    }
}
