package com.kob.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    private Integer id;
    private  Integer sx;//玩家信息 初始位置 => Game.playerA =>websocket.respGame =>前端pk.js.updateGme;
    private  Integer sy;
    private List<Integer> steps; //玩家每走一步的方向





    private  boolean check_tail_increasing(int step){//检验当前回合蛇是否边长
        if(step <= 10) return  true;
        return  step%3 == 1;

    }



    public List<Cell> getCells(){//来写蛇身子
        List<Cell> res= new ArrayList<>();
        int []dx = {-1,0,1,0},dy={0,1,0,-1};
        int x =sx,y =sy;
        int step =0;//开始步数为0
        res.add(new Cell(x,y));

        for( int d:steps){//d是下一步方向，冲steps取出来
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x,y));
            if( !check_tail_increasing( ++step)){//如果不符合蛇身子边长，则去除最后一步
                res.remove(0);
            }


        }
        return res;

    }

    public String getStepsString(){//将steps 转化为字符串=>上传数据库
        StringBuilder res = new StringBuilder();
        for (int d:steps){
            res.append(d);

        }
        return res.toString();
    }




}
