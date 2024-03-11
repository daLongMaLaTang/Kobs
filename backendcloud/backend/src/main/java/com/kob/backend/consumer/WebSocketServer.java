package com.kob.backend.consumer;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.utils.Game;
import com.kob.backend.consumer.utils.JwtAuthentication;
import com.kob.backend.mapper.RecordMapper;
import com.kob.backend.mapper.UserMapper;
import com.kob.backend.pojo.User;
import io.jsonwebtoken.io.IOException;
import jakarta.websocket.*;
import jakarta.websocket.OnMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint("/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private User user;
    //一个全局变量用于存储所有的对战链接，这里用ConcurrentHashMap来保证安全性
    final  public   static ConcurrentHashMap<Integer,WebSocketServer> users =new ConcurrentHashMap<>();

    private  Session session =null; //session用于后端向前端发信息
    private static UserMapper userMapper;

    public  static RecordMapper recordMapper;
    private Game game =null;
    private static RestTemplate restTemplate;
    private final  static String addPlayerUrl ="http://127.0.0.1:3001/player/add/";
    private final  static String removePlayerUrl ="http://127.0.0.1:3001/player/remove/";




    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate =restTemplate;
    }


    @Autowired
    //注入数据库，由于websocket不是springboot的组件，所以不能直接加，而是写一个set方法
    public  void setUserMapper(UserMapper userMapper){
        WebSocketServer.userMapper =userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper){
        WebSocketServer.recordMapper =recordMapper;
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

        }
        // 关闭链接
    }



    public static void startGame(Integer aId,Integer bId){
        User a = userMapper.selectById(aId),b=userMapper.selectById(bId);
        Game game = new Game(13,14,20,a.getId(),b.getId());
        game.createMap();
        game.start();//另起一个线程=> run()
        if(users.get(a.getId())!=null);{
            users.get(a.getId()).game =game;

        }
        if(users.get(b.getId())!=null);
        {
            users.get(b.getId()).game = game;//启动该线程后要将a，b的信息返回}
        }
        JSONObject respGame = new JSONObject();
        respGame.put("a_id",game.getPlayerA().getId());
        respGame.put("a_sx",game.getPlayerA().getSx());
        respGame.put("a_sy",game.getPlayerA().getSy());

        respGame.put("b_id",game.getPlayerB().getId());
        respGame.put("b_sx",game.getPlayerB().getSx());
        respGame.put("b_sy",game.getPlayerB().getSy());
        respGame.put("map",game.getG());


        //匹配成功需要给用户ab返回信息
        JSONObject respA =new JSONObject();//这里要返回信息到前端
        respA.put("event","start-matching");
        respA.put("opponent_username",b.getUsername());
        respA.put("opponent_photo",b.getPhoto());
        respA.put("game",respGame);// a 获取ditu以及下一步
        if(users.get(a.getId())!=null);
        {
            users.get(a.getId()).sendMessage(respA.toJSONString());//获取a的链接,然后以a的链接向前端a发respA
        }


        JSONObject respB =new JSONObject();//这里要返回信息到前端
        respB.put("event","start-matching");
        respB.put("opponent_username",a.getUsername());
        respB.put("opponent_photo",a.getPhoto());
        respB.put("game",respGame);
        if(users.get(b.getId())!=null);
        {
            users.get(b.getId()).sendMessage(respB.toJSONString());//获取a的链接,然后以a的链接向前端a发respA

        }}



    private  void startMatching(){//开始匹配
        System.out.println("start-matching !!!!!");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        data.add("rating",this.user.getRating().toString());
        restTemplate.postForObject(addPlayerUrl,data,String.class);
    }
    private void stopMatching() {
        System.out.println("stop-matching ");
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("user_id",this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);

    }


    private void move(int direction){
        if(game.getPlayerA().getId().equals(user.getId())){
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            game.setNextStepB(direction);
        }

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
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));

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
