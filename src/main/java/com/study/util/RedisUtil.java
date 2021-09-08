package com.study.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate<Object,Object> redis;

    public void set(String key,Object value){
        redis.opsForValue().set(key,value,300, TimeUnit.SECONDS);
    }

    public Object get(String key){
        return redis.opsForValue().get(key);
    }

   public void del(String key){
        redis.delete(key);
   }

}
