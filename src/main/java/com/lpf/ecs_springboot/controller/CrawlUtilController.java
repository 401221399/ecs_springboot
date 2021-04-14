package com.lpf.ecs_springboot.controller;

import com.google.gson.JsonObject;
import com.lpf.ecs_springboot.common.HttpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.json.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/CrawlUtil")
public class CrawlUtilController {
    public static Logger logger = LoggerFactory.getLogger(CrawlUtilController.class);


    //Get爬虫请求
    @CrossOrigin
    @RequestMapping(value="/sendByGet",method= RequestMethod.POST)
    @ResponseBody
    public Map sendByGet(@RequestBody Map param){
        String url = (String) param.get("url");
        logger.info("请求地址："+url);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        //注入header
        String headerString = (String) param.get("header");
        if (headerString != null && !headerString.equals(""))
        {
            JSONObject header = JSONObject.fromObject(headerString);
            logger.info("请求header："+headerString);
            for (Object key : header.keySet())
            {
                httpGet.addHeader((String) key, (String) header.get(key));
            }
        }

        JSONObject jsonObject = null;
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity =  response.getEntity();
            if(entity!=null){
                String result = EntityUtils.toString(entity);
                logger.info("返回参数："+result);
                JSONObject returnHeader = new JSONObject();
                for(Header h : response.getAllHeaders())
                {
                    returnHeader.put(h.getName(),h.getValue());
                }
                result = result.replaceAll("null","\"\"");
                jsonObject = JSONObject.fromObject(result);
                jsonObject.put("header",returnHeader);
            }
        } catch (IOException e) {
            logger.error("sendByGet异常："+e.getMessage(),e);
        }
        return jsonObject;
    }

    //Post爬虫请求
    @CrossOrigin
    @RequestMapping(value="/sendByPost",method= RequestMethod.POST)
    @ResponseBody
    public Map sendByPost(@RequestBody Map param){
        String url = (String) param.get("url");
        logger.info("请求地址："+url);
        String data = JSONObject.fromObject(param.get("data")).toString();
        boolean isJson = (boolean) param.get("isJson");
        logger.info("请求是否是用json方式："+isJson);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        //注入header
        String headerString = (String) param.get("header");
        if (headerString != null && !headerString.equals(""))
        {
            JSONObject header = JSONObject.fromObject(headerString);
            logger.info("请求header："+headerString);
            for (Object key : header.keySet())
            {
                httpPost.addHeader((String) key, (String) header.get(key));
            }
        }
        JSONObject jsonObject = null;
        try {
            if(isJson){//json格式
                httpPost.addHeader("Content-Type", "application/json");
                StringEntity Entity = new StringEntity(data, ContentType.create("application/json", "UTF-8"));
                httpPost.setEntity(Entity);
            }
            else {//urlencoded格式
                httpPost.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                JSONObject tempItem = JSONObject.fromObject(data);
                List<NameValuePair> list=new ArrayList<>();
                for (Object key : tempItem.keySet())
                {
                    if(tempItem.get(key) instanceof JSONArray)
                    {
                        JSONArray List =JSONArray.fromObject(tempItem.get(key));
                        for(int i=0;i<List.size();i++)
                        {
                            list.add(new BasicNameValuePair(key.toString(),List.get(i).toString()));
                        }
                    }
                    else {
                        list.add(new BasicNameValuePair(key.toString(),tempItem.get(key).toString()));
                    }

                }
                UrlEncodedFormEntity postParam=new UrlEncodedFormEntity(list,"UTF-8");
                httpPost.setEntity(postParam);
            }
            logger.info("请求入参："+EntityUtils.toString(httpPost.getEntity()));
            CloseableHttpResponse response = httpclient.execute(httpPost);
            String result = EntityUtils.toString(response.getEntity(),"utf-8");
            logger.info("返回参数："+result);
            JSONObject returnHeader = new JSONObject();
            for(Header h : response.getAllHeaders())
            {
                returnHeader.put(h.getName(),h.getValue());
            }
            result = result.replaceAll("null","\"\"");
            jsonObject =JSONObject.fromObject(result);
            jsonObject.put("header",returnHeader);
        } catch (IOException e) {
            logger.error("sendByPost异常："+e.getMessage(),e);
        }
        return jsonObject;
    }
}
