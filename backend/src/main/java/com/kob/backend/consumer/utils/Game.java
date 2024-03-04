package com.kob.backend.consumer.utils;

import java.util.Random;

public class Game {
    final private Integer rows,cols;
    final private  Integer inner_walls_count;
    final private int[][]g;//g是地图

    final private static int[]dx = {-1, 0, 1, 0},dy={0,1,0,-1};//辅助数组
     public Game(Integer rows,Integer cols,Integer inner_walls_count){
        this.rows =rows;
        this.cols =cols;
        this.inner_walls_count = inner_walls_count;
        this.g =new int[rows][cols];
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



        //测试连通性







    }

    public void  createMap(){//随机1000次数
        for (int i=0;i<1000;i++){
            if(draw())
                break;
        }

    }
}
