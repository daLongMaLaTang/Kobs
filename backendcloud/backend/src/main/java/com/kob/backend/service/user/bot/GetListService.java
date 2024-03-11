package com.kob.backend.service.user.bot;

import com.kob.backend.pojo.Bot;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface GetListService {
    List<Bot> getList();
}
