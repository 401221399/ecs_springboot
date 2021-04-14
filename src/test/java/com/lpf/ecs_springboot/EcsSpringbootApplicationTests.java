package com.lpf.ecs_springboot;

import com.alibaba.fastjson.JSONArray;
import com.lpf.ecs_springboot.common.BqgCrowl;
import com.lpf.ecs_springboot.common.CartoonCrowl_coco;
import com.lpf.ecs_springboot.common.HttpUtil;
import net.sf.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class  EcsSpringbootApplicationTests {

    @Test
    public void Test() throws Exception {
        System.out.println("Test");
    }
}

