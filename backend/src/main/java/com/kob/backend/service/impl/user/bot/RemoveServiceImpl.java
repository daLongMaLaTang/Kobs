package com.kob.backend.service.impl.user.bot;

import com.kob.backend.mapper.BotMapper;
import com.kob.backend.pojo.Bot;
import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.UserDetailsImpl;
import com.kob.backend.service.user.bot.RemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RemoveServiceImpl implements RemoveService {
    @Autowired
    private BotMapper botMapper;//取出当前id


    @Override
    public Map<String, String> remove(Map<String, String> data) {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();//得到用户的上下文信息
        UserDetailsImpl loginUser= (UserDetailsImpl) authentication.getPrincipal();
        User user =loginUser.getUser();//冲数据库中取出user的信息

        int bot_id =Integer.parseInt(data.get("bot_id"));//输入的将被删除的botid
        Bot bot =botMapper.selectById(bot_id);//冲数据库中取得上面输入的botid信息，这里的bot代表id为（）的bot
        Map<String,String> map = new HashMap<>();
        
        if(bot == null){
            map.put("error_message","bot不存在或者已被删除");
            return map;
            
        }

        if(!bot.getUserId().equals(user.getId())){
            map.put("error_message","没有权限删除该Bot");
            return map;
        }

        botMapper.deleteById(bot_id);
        map.put("error_message","success");


        return map;
    }
}
