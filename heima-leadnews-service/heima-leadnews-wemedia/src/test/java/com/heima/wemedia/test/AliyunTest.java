package com.heima.wemedia.test;

import com.heima.common.aliyun.GreenTextScan;
import com.heima.wemedia.WemediaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = WemediaApplication.class)
@RunWith(SpringRunner.class)
public class AliyunTest {

    @Autowired
    private GreenTextScan greenTextScan;

    @Test
    public void testScan() throws Exception {
        String content = "你妈死了";
        System.out.println(greenTextScan.greeTextScan(content));
    }
}
