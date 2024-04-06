package com.heima.schedule.test;

import com.heima.common.redis.CacheService;
import com.heima.schedule.ScheduleApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@SpringBootTest(classes = ScheduleApplication.class)
@RunWith(SpringRunner.class)
public class RedisTest {

    @Autowired
    private CacheService cacheService;

    @Test
    public void testList() {
        System.out.println(cacheService.lRightPop("list_001"));

    }

    @Test
    public void testZset() {
        cacheService.zAdd("zset_key_001", "hello zset 001", 1000);
        cacheService.zAdd("zset_key_001", "hello zset 002", 8888);
        cacheService.zAdd("zset_key_001", "hello zset 003", 7777);
        cacheService.zAdd("zset_key_001", "hello zset 004", 99999);
    }

    @Test
    public void testKeys(){
        Set<String> keys = cacheService.scan("future_*");
        System.out.println(keys);

        Set<String> scan = cacheService.scan("future_*");
        System.out.println(scan);
    }


}
