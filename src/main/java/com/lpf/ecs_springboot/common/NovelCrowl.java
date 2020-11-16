package com.lpf.ecs_springboot.common;

import com.alibaba.fastjson.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.URLDecoder;
import java.net.URLEncoder;
public class NovelCrowl {
    public static Logger log= LoggerFactory.getLogger(NovelCrowl.class);

    public Map Search(String word)
    {
        HashMap resuletMap = new HashMap();
        List resultList = new ArrayList();
        try {
            String searchUrl = "https://www.52bqg.net/modules/article/search.php?searchkey="+URLEncoder.encode(word,"gb2312");
            log.info("爬取："+searchUrl);
            Document document = Jsoup.connect(searchUrl).header("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36").get();
            Elements searchlist = document.select("#main li");
            if(searchlist.size()>0)
            {
                for(Element item:searchlist)
                {
                    HashMap returnObj = new HashMap();
                    returnObj.put("name",item.select(".s2 a").text());
                    returnObj.put("url",item.select(".s2 a").attr("href"));
                    returnObj.put("author",item.select(".s4").text());
                    resultList.add(returnObj);
                }
            }
            else{
                if(document.title().equals("笔趣阁")){

                }
                else {
                    Connection.Response response = Jsoup.connect(searchUrl).followRedirects(true).execute();
                    HashMap returnObj = new HashMap();
                    returnObj.put("name",document.select("#info > h1").text());
                    returnObj.put("url",response.url());
                    returnObj.put("author",document.select("#info > p:nth-child(2) > a").text());
                    resultList.add(returnObj);
                }
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
            String updata_wordnum = document.select("#info > p:nth-child(4)").text();
            String updataTime = updata_wordnum.substring(5,updata_wordnum.indexOf("["));
            String wordnum = updata_wordnum.substring(updata_wordnum.indexOf("[")+2,updata_wordnum.indexOf("字"));
            List catalogList =new ArrayList();
            for(Element item : document.select("#list > dl > dd a"))
            {
                HashMap catalog = new HashMap();
                catalog.put("url",url+item.attr("href"));
                catalog.put("name",item.text());
                catalogList.add(catalog);
            }

            resuletMap.put("name",document.select("#info > h1").text());
            resuletMap.put("img",document.select("#fmimg > img").attr("src"));
            resuletMap.put("url",document.location());
            resuletMap.put("author",document.select("#info > p:nth-child(2) > a").text());
            resuletMap.put("profile",document.select("#intro").text());
            resuletMap.put("updataTime",updataTime);
            resuletMap.put("wordnum",wordnum);
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
            resuletMap.put("catalog",document.select("#box_con > div.bookname > h1").html());
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
