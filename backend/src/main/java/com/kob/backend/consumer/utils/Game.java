package com.kob.backend.consumer.utils;

import com.alibaba.fastjson.JSONObject;
import com.kob.backend.consumer.WebSocketServer;
import com.kob.backend.pojo.Record;
import org.springframework.validation.beanvalidation.SpringConstraintValidatorFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread {
    final private Integer rows,cols;
    final private  Integer inner_walls_count;
    final private int[][]g;//g是地图
    private final  Player playerA,playerB;
    private Integer nextStepA =null;//A的下一步操作
    private Integer nextStepB =null;
    private ReentrantLock lock =new ReentrantLock();
    private String status ="playing";//playing->finished
    private String loser ="";//all 平局 A:a输
    final private static int[]dx = {-1, 0, 1, 0},dy={0,1,0,-1};//辅助数组





     public Game(Integer rows,Integer cols,Integer inner_walls_count, Integer idA,Integer idB){
        this.rows =rows;
        this.cols =cols;
        this.inner_walls_count = inner_walls_count;
        this.g =new int[rows][cols];
        playerA = new Player(idA,this.rows-2,1,new ArrayList<>());
        playerB = new Player(idB,1,this.cols-2,new ArrayList<>());
    }

    //在game中能访问到playerab
    public Player getPlayerA(){
         return  playerA;
    }
    public Player getPlayerB(){
        return  playerB;
    }


    //获取A的下一步操作
    public void setNextStepA(Integer nextStepA){
         lock.lock();
         try {
             this.nextStepA =nextStepA;
         }
         finally {
             lock.unlock();
         }
    }
    public void setNextStepB(Integer nextStepB){
        lock.lock();
        try {
            this.nextStepB =nextStepB;
        }
        finally {
            lock.unlock();
        }
    }


    //返回地图
    public int[][] getG() {
        return g;
    }
    private boolean check_connectivity(int sx,int sy,int tx,int ty){
         if(sx == tx && sy ==ty ) {
             return true;//递归出口
         }


         g[sx][sy] =1 ;//走过的路变成墙
         for(int i =0 ;i<4;i++){
             int x = sx +dx[i];
             int y = sy+ dy[i];
             if(x>=0 && x<this.rows &&y>=0 && y<this.cols &&g[x][y]==0){
                 if (check_connectivity(x,y,tx,ty)){
                     g[sx][sy] =0;//恢复现场
                     return true;
                 }

             }

         }
         g[sx][sy] = 0;
         return false;



    }

    private boolean  draw() {//初始化一个地图
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }//初始化全是路的一个地图


        for (int r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = 1; //1是墙 0是空地
        }
        for (int c = 0; c < this.rows; c++) {
            g[0][c] = g[this.rows-1][c] = 1;
        }//创建四周的墙

        //创建随机障碍物
        Random random =new Random();
        for(int i=0;i<this.inner_walls_count/2;i++){
            for(int j =0;j<1000;j++){
                int r = random.nextInt(this.rows);//r返回的是0到this.rows-1的随机值
                int c =random.nextInt(this.cols);

                if(g[r][c] == 1 ||g[this.rows-1-r][this.cols-1-c] ==1){
                    continue;//如果已经是墙了，则继续
                }
                if(r == this.rows-2 && c==1 || r == 1 && c == this.cols - 2){
                    continue;//因为四周都是墙，所以继续造枪，不用管
                }
                g[r][c] =g[this.rows-1-r][this.cols -1 -c] =1;//创建一个中心对称的障碍物
                break;
            }
        }
        return check_connectivity(this.rows-2,1,1,this.cols-2);

    }

    public void  createMap(){//随机1000次数
        for (int i=0;i<1000;i++){
            if(draw())
                break;
        }

    }
    private boolean nextStep() {//等待俩名玩家的下一次操作
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(100);
                lock.lock();
                try {
                    if(nextStepA !=null && nextStepB != null){
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return  true;
                    }
                }finally {
                    lock.unlock();

                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }



        }
        return false;
    }

    private boolean check_valid(List<Cell> cellsA,List<Cell> cellsB){
        int n = cellsA.size();//判断第一个传进来的蛇 是否合法
        Cell cell = cellsA.get(n-1);//蛇的最后一位是否是墙，即新蛇头
        if(g[cell.x][cell.y] == 1 ) return false;
        for(int i=0;i < n-1;i++){
            if(cellsA.get(i).x == cell.x && cellsA.get(i).y == cell.y)
                return false;
        }
        for(int i=0;i<n-1;i++){
            if(cellsB.get(i).x == cell.x && cellsB.get(i).y == cell.y)
                return false;
        }
        return  true;

    }
    private  void judge(){
        //判断俩名玩家操作是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA =check_valid(cellsA,cellsB);
        boolean validB =check_valid(cellsB,cellsA);
        System.out.println(validA);
        System.out.println(validB);
        if(!validA||!validB){
            status ="finished";
            if(!validA && !validB ){
                loser ="all";
            } else if (!validA) {
                loser ="A";
            }
            else {
                loser ="B";
            }
        }
    }




    private void sendMove(){//向前端2个用户传递信息
         lock.lock();
         try {
             JSONObject resp = new JSONObject();
             resp.put("event","move");
             resp.put("a_direction",nextStepA);
             resp.put("b_direction",nextStepB);
             sendAllMessage(resp.toJSONString());
             nextStepA =nextStepB =null;//向前端传过去后，清空操作

         }finally {
             lock.unlock();
         }

    }

    private String getMapString(){//将地图转为字符串 =>上传数据库
         StringBuilder res = new StringBuilder();
         for(int i =0 ;i<rows;i++){
             for (int j =0;j<cols;j++){
                 res.append(g[i][j]);
             }

         }
         return  res.toString();
    }

    private  void  saveToDatebase(){//在向前端发送消息前先给数据库发过去
         //1 先往websocket里注入RecordMapper
        Record record  = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                loser,
                new Date()

        );
        WebSocketServer.recordMapper.insert(record);

    }


    private void sendResult(){
         //向俩名玩家返回结果
        JSONObject resp = new JSONObject();
        resp.put("event","result");
        resp.put("loser",loser);
        saveToDatebase();
        sendAllMessage(resp.toJSONString());



    }

    private void sendAllMessage(String message){  //用来给 用户ab发送信息（通过链接）
        WebSocketServer.users.get(playerA.getId()).sendMessage(message);//a的链接 sendmessage
        WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }




    @Override
    public void run() {//在 run() 方法中，你可以编写线程的执行逻辑，例如调用其他方法、循环、条件判断等。当线程执行完 run() 方法中的代码后，线程就会结束。

         for(int i = 0; i<5000;i++){

            if(nextStep()){//是否获取到俩条蛇的下一步操作
                judge();

                if(status.equals("playing")){
                    sendMove();
                }
                else {
                    sendResult();
                    break;
                }

            }
            else {
                status ="finished";
                lock.lock();
                try {
                    if(nextStepA == null && nextStepB == null){
                        loser ="all";
                    } else if (nextStepA == null) {
                        loser="A";
                    }
                    else {
                        loser ="B";
                    }
                }
                finally {
                    lock.unlock();
                }
                sendResult();
                break;
            }
        }
    }

}
