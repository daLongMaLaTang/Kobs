const AC_GAME_OBJECTS = [];

export class AcGameObject{
    constructor(){
        AC_GAME_OBJECTS.push(this);
        this.has_callded_start =false;
        this.timedelta =0; //俩帧之间的时间间隔
    }


    start(){//只执行一次


    }
    update(){ //除了第一帧 每一帧执行一次

    }

    on_destroy(){//删除之前执行
        

    }



    destory(){
        this.on_destroy();

        for(let i in AC_GAME_OBJECTS){
            const obj =AC_GAME_OBJECTS[i];
            if(obj === this){
                AC_GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}


let last_timestamp;//上一次执行时刻
const step = timestamp => {//timestamp 当前执行的时刻
    for(let obj of AC_GAME_OBJECTS){
        if(!obj.has_callded_start){
            obj.has_callded_start = true;
            obj.start();
        }
        else{ 
            obj.timedelta =timestamp - last_timestamp;
            obj.update();
        }

        
    }
    last_timestamp =timestamp;


    requestAnimationFrame(step)
}

requestAnimationFrame(step) //下一帧执行step函数