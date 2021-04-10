package com.lpf.ecs_springboot.common;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import okhttp3.*;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class HttpUtil {
    public static Logger logger = LoggerFactory.getLogger(com.lpf.ecs_springboot.common.HttpUtil.class);

    public static JSONObject doGetstr(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        JSONObject jsonObject = null;
        logger.info("请求地址："+url);
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity =  response.getEntity();
            if(entity!=null){
                String result = EntityUtils.toString(entity);
                logger.info("返回参数："+result);
                result = result.replaceAll("null","\"\"");
                jsonObject = JSONObject.fromObject(result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;

    }
    public static String doGetHtml(String url){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
//        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
//        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//        httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7");
//        httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
//        httpGet.addHeader("Cache-Control", "no-cache");
//        httpGet.addHeader("Connection", "keep-alive");
//        httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
//        httpGet.addHeader("Host", "www.52bqg.net");
//        httpGet.addHeader("Pragma", "no-cache");
//        httpGet.addHeader("Sec-Fetch-Dest", "document");
//        httpGet.addHeader("Sec-Fetch-Mode", "navigate");
//        httpGet.addHeader("Sec-Fetch-Site", "cross-site");
//        httpGet.addHeader("Sec-Fetch-User", "?1");
//        httpGet.addHeader("Upgrade-Insecure-Requests", "1");
        String result = null;
        JSONObject json = null;
        logger.info("请求地址："+url);
        try {
            CloseableHttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity =  response.getEntity();
            if(entity!=null){
                result = EntityUtils.toString(entity);
                result = new String(result.getBytes("ISO-8859-1"),"gbk");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static JSONObject doGetWithCookies(String url, Map<String,String> cookies) {
        JSONObject json = null;
        String cookiesText ="";
        for(String key : cookies.keySet())
        {
            cookiesText = cookiesText + key+"="+cookies.get(key)+"; ";
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .addHeader("Cookie", cookiesText)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            json = JSONObject.fromObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject doPostWithCookies(String url, Map<String,String> cookies,Map<String,String> body) {
        JSONObject json = null;
        String cookiesText ="";
        for(String key : cookies.keySet())
        {
            cookiesText = cookiesText + key+"="+cookies.get(key)+"; ";
        }
        String posturl = url + "?";
        for(String key : body.keySet())
        {
            posturl = posturl +key +"="+body.get(key)+"&";
        }
        posturl = posturl.substring(0,posturl.length()-1);
        System.out.println(posturl);
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody bodytmp = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(posturl)
                .method("POST", bodytmp)
                .addHeader("Cookie", cookiesText)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            json = JSONObject.fromObject(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static JSONObject doPoststr(String url,String outStr,boolean isJson){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        JSONObject jsonObject = null;
        logger.info("请求地址："+url);
        try {
            if(isJson){//json格式
                httpPost.addHeader("Content-Type", "application/json");
                StringEntity Entity = new StringEntity(outStr, ContentType.create("application/json", "UTF-8"));
                httpPost.setEntity(Entity);
            }
            else {//urlencoded格式
                httpPost.addHeader("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                JSONObject tempItem = JSONObject.fromObject(outStr);
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
            for(Header h : response.getAllHeaders())
            {
                logger.info(h.getName()+"----"+h.getValue());
            }
            result = result.replaceAll("null","\"\"");
            jsonObject =JSONObject.fromObject(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
