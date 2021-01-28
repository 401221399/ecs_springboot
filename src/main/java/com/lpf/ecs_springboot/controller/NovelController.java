package com.lpf.ecs_springboot.controller;;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lpf.ecs_springboot.common.BqgCrowl;
import com.lpf.ecs_springboot.common.NovelCrowl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/novel")
public class NovelController {
    public static Logger logger = LoggerFactory.getLogger(NovelController.class);
    private NovelCrowl novelCrowl;
    private BqgCrowl bqgCrowl;
    public NovelController(){
       novelCrowl = new NovelCrowl();
       bqgCrowl = new BqgCrowl();
    }

    @CrossOrigin
    @RequestMapping(value="/getTarget",method= RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getTarget(HttpServletRequest request)throws Exception{
        String TargetList = "[{\"name\":\"笔趣阁\",\"value\":\"\"},{\"name\":\"新笔趣阁\",\"value\":\"xbqg\"}]";
        HashMap resuletMap = new HashMap();
        resuletMap.put("flag","ok");
        resuletMap.put("data",JSONObject.parseArray(TargetList));
        return resuletMap;
    }

    @CrossOrigin
    @RequestMapping(value="/search",method= RequestMethod.GET)
    @ResponseBody
    public Map search(HttpServletRequest request)throws Exception{
        String word = request.getParameter("word");
        String target = request.getParameter("target");
        if (null != target && "xbqg".equals(target))
        {
            return bqgCrowl.Search(word);
        }
        return novelCrowl.Search(word);

    }

    @CrossOrigin
    @RequestMapping(value="/getNovelAttr")
    @ResponseBody
    public Map getNovelAttr(@RequestBody Map param)throws Exception{
        String url = String.valueOf(param.get("url"));
        if (url.indexOf("www.xbiquge.la")>=0)
        {
            return bqgCrowl.getNovelAtrr(url);
        }
        return novelCrowl.getNovelAtrr(url);
    }

    @CrossOrigin
    @RequestMapping(value="/openCatalog")
    @ResponseBody
    public Map openCatalog(@RequestBody Map param)throws Exception{
        String url = String.valueOf(param.get("url"));
        if (url.indexOf("www.xbiquge.la")>=0)
        {
            return bqgCrowl.openCatalog(url);
        }
        return novelCrowl.openCatalog(url);
    }
}
