package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import io.jsonwebtoken.io.IOException;
import jakarta.websocket.*;
import jakarta.websocket.OnMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private User user;
    //一个全局变量用于存储所有的对战链接，这里用ConcurrentHashMap来保证安全性
    final  private  static ConcurrentHashMap<Integer,WebSocketServer> users =new ConcurrentHashMap<>();

    private  Session session =null; //session用于后端向前端发信息


    private static UserMapper userMapper;



    final private static CopyOnWriteArraySet<User> matchpool = new CopyOnWriteArraySet<>();//新建立一个匹配池！！！！





    @Autowired
    //注入数据库，由于websocket不是springboot的组件，所以不能直接加，而是写一个set方法
    public  void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper =userMapper;
    }
    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) {
        // 建立连接
        this.session =session;
        System.out.println("connected!");
        Integer userId = JwtAuthentication.getUserId(token);
        this.user=userMapper.selectById(userId);//在onOpen获取到数据库的id

        if(this.user != null){
            users.put(userId,this);//将获取到的userID放到匹配池中
        }
        else {
            try {
                this.session.close();
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(users);


    }

    @OnClose
    public void onClose() {
        System.out.println("disconnected!");
        if(this.user!= null){//如果关闭连接，则需要将所有用户t出匹配池
            users.remove(this.user.getId());
            matchpool.remove(this.user);
        }
        // 关闭链接
    }
    private  void startMatching(){//开始匹配
        System.out.println("start-matching");
        matchpool.add(this.user);
        while (matchpool.size()>=2){
            Iterator<User> it =matchpool.iterator();//iterator是配对
            User a =it.next();
            User b =it.next();
            matchpool.remove(a);
            matchpool.remove(b);

            Game game = new Game(13,14,20);
            game.createMap();


            //匹配成功需要给用户ab返回信息
            JSONObject respA =new JSONObject();//这里要返回信息到前端
            respA.put("event","start-matching");
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("gamemap",game.getG());
            users.get(a.getId()).sendMessage(respA.toJSONString());//获取a的链接,然后以a的链接向前端a发respA

            JSONObject respB =new JSONObject();//这里要返回信息到前端
            respB.put("event","start-matching");
            respB.put("opponent_username",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("gamemap",game.getG());
            users.get(b.getId()).sendMessage(respB.toJSONString());//获取a的链接,然后以a的链接向前端a发respA
        }
    }
    private void stopMatching() {
        System.out.println("stop-matching ");
        matchpool.remove(this.user);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // 前端向后短发信息，会触发onMessage,用来接受请求来自前端发送的信息 即event
        System.out.println("receive message!");
        //接收到请求后利用json来解析信息 然后赋值给 event
        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");
        if("start-matching".equals(event)){
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        }
    }



    public void sendMessage(String message){
        //后端向前端发信息
        synchronized (this.session){
            try{
                this.session.getAsyncRemote().sendText(message);
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }
    }




    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

}
