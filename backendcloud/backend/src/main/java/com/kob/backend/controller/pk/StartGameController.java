package com.kob.backend.controller.pk;

import com.kob.backend.service.pk.StartGameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class StartGameController {
    @Autowired
    private StartGameService startGameService;
    //此时匹配池已经完成了匹配逻辑，需要将结果返回到backend中
    //所以需要与1个api来接受该结果

    @PostMapping("/pk/start/game/")
    public String startGame(@RequestParam MultiValueMap<String,String> data){
        Integer aId =Integer.parseInt(Objects.requireNonNull(data.getFirst("a_id")));
        Integer bId =Integer.parseInt(Objects.requireNonNull(data.getFirst("b_id")));
        return startGameService.startGame(aId,bId);//把结果放到matching后端的sendResult中


    }

}
