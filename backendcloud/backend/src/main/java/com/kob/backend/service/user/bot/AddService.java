package com.kob.backend.service.user.bot;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AddService {
    public Map<String,String> add(Map<String,String> data);
}
