package com.kob.backend.service.user.bot;

import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public interface RemoveService {
    Map<String,String> remove (Map<String,String> data);
}
