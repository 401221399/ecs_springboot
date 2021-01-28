package com.lpf.ecs_springboot.common;

import com.alibaba.fastjson.JSON;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BqgCrowl {
    public static Logger log= LoggerFactory.getLogger(BqgCrowl.class);

    public Map Search(String word)
    {
        HashMap resuletMap = new HashMap();
        List resultList = new ArrayList();
        try {
            String searchUrl = "http://www.xbiquge.la/modules/article/waps.php?searchkey="+URLEncoder.encode(word,"UTF-8");
            log.info("爬取："+searchUrl);
            Connection c = Jsoup.connect(searchUrl);
            c.data("searchkey", word);
            Document document = c.header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36").post();
            Elements searchlist = document.select("#checkform > table > tbody > tr");
            if(searchlist.size()>0)
            {
                for(Element item:searchlist)
                {
                    if (item.attr("align").indexOf("center")>= 0)
                    {
                        continue;
                    }
                    HashMap returnObj = new HashMap();
                    returnObj.put("name",item.select("td:nth-child(1) > a").text());
                    returnObj.put("url",item.select("td:nth-child(1) > a").attr("href"));
                    returnObj.put("author",item.select("td:nth-child(3)").text());
                    resultList.add(returnObj);
                }
            }
            else{
                resuletMap.put("msg","没找到相关的小说");
                resuletMap.put("flag","error");
            }
            resuletMap.put("count",resultList.size());
            resuletMap.put("data",resultList);
            resuletMap.put("flag","ok");
            log.info("爬取结果"+ JSON.toJSON(resuletMap));
        } catch (Exception e) {
            log.error(e.getMessage());
            resuletMap.put("msg",e.getMessage());
            resuletMap.put("flag","error");
        }
        return resuletMap;
    }

    public Map getNovelAtrr(String url)
    {
        HashMap resuletMap = new HashMap();

        try {
            log.info("爬取："+url);
            Document document = Jsoup.connect(url).header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36").get();
            List catalogList =new ArrayList();
            for(Element item : document.select("#list > dl > dd a"))
            {
                HashMap catalog = new HashMap();
                catalog.put("url",item.attr("abs:href"));
                catalog.put("name",item.text());
                catalogList.add(catalog);
            }

            resuletMap.put("name",document.select("#info > h1").text());
            resuletMap.put("img",document.select("#fmimg > img").attr("src"));
            resuletMap.put("url",document.location());
            resuletMap.put("author",splitAttr(document.select("#info > p:nth-child(2)").text()));
            resuletMap.put("profile",document.select("#intro > p:nth-child(2)").text());
            resuletMap.put("updataTime",splitAttr(document.select("#info > p:nth-child(4)").text()));
            resuletMap.put("wordnum","");
            resuletMap.put("catalogList",catalogList);
            resuletMap.put("flag","ok");
            log.info("爬取结果"+ JSON.toJSON(resuletMap));
        } catch (Exception e) {
            log.error(e.getMessage());
            resuletMap.put("msg",e.getMessage());
            resuletMap.put("flag","error");
        }
        return resuletMap;
    }

    private String splitAttr(String str){
        int sindex = str.indexOf("：");
        String value = str.substring(sindex+1,str.length());
        return value;
    };

    public Map openCatalog(String url)
    {
        HashMap resuletMap = new HashMap();
        try {
            log.info("爬取："+url);
            Document document = Jsoup.connect(url).header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36").get();
            String content = document.select("#content").html();
            content = content.replaceAll("&nbsp;&nbsp;","　");
            content = content.replaceAll("\\n","");
            content = content.replaceAll("一秒记住【笔趣阁 www.52bqg.net】，精彩小说无弹窗免费阅读！<br><br>","");
            resuletMap.put("content",content);
            resuletMap.put("catalog",document.select("#wrapper > div.content_read > div > div.bookname > h1").html());
            resuletMap.put("flag","ok");
            log.info("爬取结果"+ JSON.toJSON(resuletMap));
        } catch (Exception e) {
            log.error(e.getMessage());
            resuletMap.put("msg",e.getMessage());
            resuletMap.put("flag","error");
        }
        return resuletMap;
    }
}
