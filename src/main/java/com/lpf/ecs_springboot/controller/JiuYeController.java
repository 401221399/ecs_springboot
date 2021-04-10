package com.lpf.ecs_springboot.controller;

import com.lpf.ecs_springboot.common.CartoonCrowl_coco;
import com.lpf.ecs_springboot.common.HttpUtil;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;
//九野卡组模拟器
@Controller
@RequestMapping("/JiuYe")
public class JiuYeController {
    public static Logger logger = LoggerFactory.getLogger(JiuYeController.class);


    //获取属性集合
    @CrossOrigin
    @RequestMapping(value="/getshuxing",method= RequestMethod.POST)
    @ResponseBody
    public Map getshuxing(){
        String url = "https://www.shengli.com/jycard/getshuxing";
        return HttpUtil.doGetstr(url);
    }

    //获取角色属性集合
    @CrossOrigin
    @RequestMapping(value="/getrole")
    @ResponseBody
    public Map getrole(@RequestBody Map param){
        String id = String.valueOf(param.get("id"));
        String url = "https://www.shengli.com/jycard/getrole?id="+id;
        return HttpUtil.doGetstr(url);
    }

    //获取卡牌属性集合
    @CrossOrigin
    @RequestMapping(value="/getcard")
    @ResponseBody
    public Map getcard(@RequestBody Map param){
        String paramStr = JSONObject.fromObject(param).toString();
        String url = "https://www.shengli.com/jycard/getcard";
        return HttpUtil.doPoststr(url,paramStr,false);
    }
}
