package com.lpf.ecs_springboot.common;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lpf.ecs_springboot.entity.Cartoon;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//爬取百年漫画
public class CartoonCrowl_bainian {
    public static Logger log= LoggerFactory.getLogger(CartoonCrowl_bainian.class);
    public Map Search(String word,String page)
    {
        JSONObject resuletMap = new JSONObject();
        try {
            List resultList = new ArrayList();
            String searchUrl = "https://m.bnmanhua.com/search/"+ URLEncoder.encode(word,"UTF-8")+"/"+page+".html";
            Document document =  Jsoup.parse(new URL(searchUrl).openStream(), "UTF-8", searchUrl);
            Elements CartoonList = document.select("body > div.tbox > ul > li");
            if(CartoonList.size()>0) {
                for (Element Cartoon : CartoonList) {
                    HashMap CartoonObj = new HashMap();
                    CartoonObj.put("name",Cartoon.select("h4 > a").text());
                    CartoonObj.put("url","https://m.bnmanhua.com"+Cartoon.select("h4 > a").attr("href"));
                    CartoonObj.put("img",Cartoon.select("a > mip-img").attr("src"));
                    resultList.add(CartoonObj);
                }
                Elements paginationLis = document.select("div.pagination li");
                if (paginationLis.size()>0)
                {
                    Element next = paginationLis.get(1);
                    if (next.attr("class").indexOf("disabled")< 0)
                    {
                        resuletMap.put("nextPage",true);
                    }
                    else {
                        resuletMap.put("nextPage",false);
                    }
                }
                else {
                    resuletMap.put("nextPage",false);
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
            Document document =  Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            Elements CatalogList = document.select("div.tabs_block > ul > li");
            if(CatalogList.size()>0) {
                for (Element  catalog : CatalogList) {
                    HashMap CatalogObj = new HashMap();
                    CatalogObj.put("name", catalog.select("a").text());
                    CatalogObj.put("url","https://m.bnmanhua.com"+catalog.select("a").attr("href"));
                    resultList.add(CatalogObj);
                }
                resuletMap.put("name",document.select("body > div.dbox > div.data > h4").text());
                resuletMap.put("img",document.select("body > div.dbox > div.img > mip-img").attr("src"));
                resuletMap.put("url",document.location());
                resuletMap.put("author",document.select("body > div.dbox > div.data > p.dir").text());
                resuletMap.put("classname",document.select("body > div.dbox > div.data > p.yac").text());
                resuletMap.put("updataTime",document.select("body > div.dbox > div.data > p.act").text());
                resuletMap.put("catalogList",resultList);
                resuletMap.put("flag","ok");
            }
            else
            {
                resuletMap.put("msg","查无结果");
                resuletMap.put("flag","error");
            }

        } catch (Exception e) {
            resuletMap.put("msg",e.getMessage());
            resuletMap.put("flag","error");
        }
        return resuletMap;
    }

    public Map getImgList(String url)
    {
        JSONObject resuletMap = new JSONObject();
        try {
            Document document =  Jsoup.parse(new URL(url).openStream(), "UTF-8", url);
            Elements jsList = document.getElementsByTag("script");
            for (Element js :jsList){
                String content = js.data().toString();
                int start = content.indexOf("var z_img");
                if (start>=0)
                {
                    int end = content.substring(start).indexOf(";")+start;
                    String s = content.substring(start,end);
                    start = s.indexOf("[");
                    end = s.substring(start).indexOf("]")+start;
                    JSONArray imgList = JSONArray.parseArray(s.substring(start,end+1));
                    resuletMap.put("imgList",imgList);
                    resuletMap.put("flag","ok");
                    resuletMap.put("count",imgList.size());
                }
            }
        } catch (Exception e) {
            resuletMap.put("msg",e.getMessage());
            resuletMap.put("flag","error");
        }
        return resuletMap;
    }

}
