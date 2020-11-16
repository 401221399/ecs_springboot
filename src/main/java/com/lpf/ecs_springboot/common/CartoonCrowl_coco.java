package com.lpf.ecs_springboot.common;

import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//爬取oh漫画
public class CartoonCrowl_coco {
    public static Logger log= LoggerFactory.getLogger(CartoonCrowl_coco.class);
    private  String domain = "https://www.cocomanhua.com";
    private String localDomain = "http://120.78.125.122:8080";


    public Map Search(String word,String page)
    {
        JSONObject resuletMap = new JSONObject();
        try {
            List resultList = new ArrayList();
            String searchUrl = domain+"/search?searchString="+ URLEncoder.encode(word,"UTF-8")+"&page="+page;
            Connection connect = Jsoup.connect(searchUrl);
            connect.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
            Document document =  connect.get();
            Elements CartoonList = document.select("dl");
            if(CartoonList.size()>0) {
                for (Element Cartoon : CartoonList) {
                    HashMap CartoonObj = new HashMap();
                    CartoonObj.put("name",Cartoon.select("dd > h1 > a").text());
                    CartoonObj.put("url",domain+Cartoon.select("dt > a").attr("href"));
                    CartoonObj.put("img",localDomain+"/cartoon/getCover"+Cartoon.select("dt > a").attr("href"));
                    resultList.add(CartoonObj);
                }
                resuletMap.put("nextPage",false);
                Elements paginationLis = document.select("div.fed-page-info > a");
                if (paginationLis.size()>0) {
                    for (Element pagination : paginationLis) {
                        if (pagination.text().equals("下页")) {
                            if (pagination.attr("class").indexOf("disad") < 0) {
                                resuletMap.put("nextPage", true);
                            }
                        }
                    }
                }
                resuletMap.put("count",resultList.size());
                resuletMap.put("data",resultList);
                resuletMap.put("flag","ok");
            }
            else
            {
                resuletMap.put("msg","查无结果");
                resuletMap.put("flag","error");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            resuletMap.put("msg",e.getMessage());
            resuletMap.put("flag","error");
        }
        return resuletMap;
    }

    public Map getCartoonAtrr(String url)
    {
        JSONObject resuletMap = new JSONObject();
        try {
            List resultList = new ArrayList();
            Connection connect = Jsoup.connect(url);
            connect.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");
            Document document =  connect.get();
            Elements CatalogList = document.select("div.all_data_list > ul > li");
            if(CatalogList.size()>0) {
                for (Element  catalog : CatalogList) {
                    HashMap CatalogObj = new HashMap();
                    CatalogObj.put("name", catalog.select("a").text());
                    CatalogObj.put("url",domain+catalog.select("a").attr("href"));
                    resultList.add(CatalogObj);
                }



                int end = url.lastIndexOf("/");
                int start = url.substring(0,end).lastIndexOf("/");
                String cartonnID = url.substring(start,end+1);
                resuletMap.put("name",document.select("dd.fed-deta-content > h1").text());
                resuletMap.put("img",localDomain+"/cartoon/getCover"+cartonnID);
                resuletMap.put("url",url);
                resuletMap.put("author",document.select("dd.fed-deta-content > ul > li").get(2).text());
                resuletMap.put("classname",document.select("dd.fed-deta-content > ul > li").get(5).text());
                resuletMap.put("updataTime",document.select("dd.fed-deta-content > ul > li").get(3).text());
                resuletMap.put("catalogList",resultList);
                resuletMap.put("flag","ok");
                resuletMap.put("catalogList",resultList);
            }
            else
            {
                resuletMap.put("msg","查无结果");
                resuletMap.put("flag","error");
            }

        } catch (Exception e) {
            e.getStackTrace();
            resuletMap.put("msg",e.getMessage());
            resuletMap.put("flag","error");
        }
        return resuletMap;
    }

    public byte[] getPic(String url) throws IOException {
        URL URL = new URL(url);
        //打开链接
        HttpURLConnection conn = (HttpURLConnection)URL.openConnection();
        //设置请求头
        conn.setRequestProperty("Referer", "https://www.cocomanhua.com/");
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.111 Safari/537.36");

        //设置请求方式为"GET"
        conn.setRequestMethod("GET");
        //超时响应时间为5秒
        conn.setConnectTimeout(5 * 1000);
        //通过输入流获取图片数据
        InputStream inStream = conn.getInputStream();

        //得到图片的二进制数据，以二进制封装得到数据，具有通用性
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        //创建一个Buffer字符串
        byte[] buffer = new byte[1024];
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        //关闭输入流
        inStream.close();
        //使用一个输入流从buffer里把数据读取出来
        byte[] data = outStream.toByteArray();
        return data;
    }

    public Map getImgList(String url)  {
        JSONObject resuletMap = new JSONObject();
        try {
            List resultList = new ArrayList();

            //获取页面
            String manga_html = Jsoup.connect(url).header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36").get().toString();

            //获取C_DATA加密文
            int start = manga_html.indexOf("C_DATA='")+8;
            int end = start+manga_html.substring(start).indexOf("'");
            String C_DATA = manga_html.substring(start,end);
            //转base64->Decrypt解密(CryptoJS,ECB模式解密)
            byte[] decoded = java.util.Base64.getDecoder().decode(C_DATA);
            String content = new String(decoded);
            String info = Decrypt(content,"fw12558899ertyui");//key通过调式页面获取

            //获取图片前缀加密文
            start = info.indexOf("enc_code2")+11;
            end = info.substring(start).indexOf("\"")+start;
            String enc_code2 = info.substring(start,end);
            //转base64->Decrypt解密(CryptoJS,ECB模式解密)
            decoded = java.util.Base64.getDecoder().decode(enc_code2);
            content = new String(decoded);
            String preurl = Decrypt(content,"fw125gjdi9ertyui");//key通过调式页面获取

            //获取页面总数加密文
            start = info.indexOf("enc_code1")+11;
            end = info.substring(start).indexOf("\"")+start;
            String enc_code1 = info.substring(start,end);
            //转base64->Decrypt解密(CryptoJS,ECB模式解密)
            decoded = java.util.Base64.getDecoder().decode(enc_code1);
            content = new String(decoded);
            String pagecount = Decrypt(content,"fw12558899ertyui");//key通过调式页面获取


            String Imgpre = localDomain+"/cartoon/getPic/"+preurl;
            //遍历得到每张图片的地址
            for (int i = 0; i<Integer.parseInt(pagecount);i++)
            {
                //整数转4位字符串"%04d",0表示自动补0,4表示为4位字符串，d为整数
                String Imgsuf = String.format("%04d", i+1)+".jpg";
                resultList.add(Imgpre+Imgsuf);
            }
            resuletMap.put("imgList",resultList);
            resuletMap.put("flag","ok");
            resuletMap.put("count",resultList.size());
        } catch (Exception e) {
            e.getStackTrace();
            resuletMap.put("msg",e.getMessage());
            resuletMap.put("flag","error");
        }
        return resuletMap;
    }

    // 加密
    public static String Encrypt(String sSrc, String sKey) throws Exception {
        if (sKey == null) {
            System.out.print("Key为空null");
            return null;
        }
        // 判断Key是否为16位
        if (sKey.length() != 16) {
            System.out.print("Key长度不是16位");
            return null;
        }
        byte[] raw = sKey.getBytes("utf-8");
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");//"算法/模式/补码方式"
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));

        return new Base64().encodeToString(encrypted);//此处使用BASE64做转码功能，同时能起到2次加密的作用。
    }

    // 解密
    public static String Decrypt(String sSrc, String sKey) throws Exception {
        try {
            // 判断Key是否正确
            if (sKey == null) {
                System.out.print("Key为空null");
                return null;
            }
            // 判断Key是否为16位
            if (sKey.length() != 16) {
                System.out.print("Key长度不是16位");
                return null;
            }
            byte[] raw = sKey.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted1 = new Base64().decode(sSrc);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original,"utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

}
