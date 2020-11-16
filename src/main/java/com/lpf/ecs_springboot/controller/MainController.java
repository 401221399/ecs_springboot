package com.lpf.ecs_springboot.controller;

import com.alibaba.fastjson.JSON;
import com.lpf.ecs_springboot.common.HttpUtil;
import com.lpf.ecs_springboot.common.R;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Map;

@Controller
@RequestMapping("")
public class MainController {

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/errorInfo")
    public R errorInfo() {
        return R.error("系统异常！");
    }


    @CrossOrigin
    @RequestMapping(value = "/aW5kZXgKIA==")
    //index
    public String index() {
        return "index";
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/NoUser")
    public R NoUser() {
        return R.error("用户没有登录！");
    }

    @CrossOrigin
    @RequestMapping(value="/Ai",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject Ai(HttpServletRequest request)throws Exception{
        String word = request.getParameter("word");
        String Word2Url = URLEncoder.encode(word,"gb2312");
        String url = "https://ai.sm.cn/quark/1/ai?&format=json&dn=33967752786-ed4d61bd&nt=6&nw=4G&ve=4.3.5.146&pf=3300&fr=android&gi=bTkwBDfTl2WRXHfwH206vaB9%252BVl36sHTGWWgwz2VY3wxSQ%253D%253D&bi=35838&pr=ucpro&sv=release&ds=AANDcUOJmVYo2NkfzU4ifDG%2BF1cZpDvFD14Yh8a7FtMdrQ%3D%3D&di=73d7567c198cabee&ch=kk%40store&ei=bTkwBOCcPJKeMJzE4hKdj9%2FqIopUIsnb5w%3D%3D&cp=isp%3A%E7%A7%BB%E5%8A%A8%3Bprov%3A%E5%B9%BF%E4%B8%9C%3Bcity%3A%E5%B9%BF%E5%B7%9E%3Bna%3A%E4%B8%AD%E5%9B%BD%3Bcc%3ACN%3Bac%3A&ni=bTkwBDzTZ4MzBOPRAW3tmqp7WqcUtn4cETE%2BMSiZ9vvvsws%3D&ut=AANDcUOJmVYo2NkfzU4ifDG%2BF1cZpDvFD14Yh8a7FtMdrQ%3D%3D&mi=RMX1901&session_id=d1ded5bf-708f-c2d9-e36d-98fa0fbf22aa&q="+Word2Url+"&query_source=text&activity_id=undefined&scene_name=&origin=113.313774%2C23.120956";
        System.out.println(url);
        String reqMap = JSON.toJSONString(HttpUtil.doGetstr(url));
        JSONObject resultMap = new JSONObject();
        resultMap.put("data",reqMap);
        resultMap.put("flag","ok");
        resultMap.put("msg","我不理解");
        if (reqMap.indexOf("guide")>=0)
        {
            int start = reqMap.indexOf("guide")+8;
            int end = start+reqMap.substring(start).indexOf(",")-1;
            resultMap.put("msg",reqMap.substring(start,end));
        }
        if (reqMap.indexOf("answer")>=0)
        {
            int start = reqMap.indexOf("answer")+9;
            int end = start+reqMap.substring(start).indexOf(",")-1;
            resultMap.put("msg",reqMap.substring(start,end));
        }
        if (reqMap.indexOf("url")>=0)
        {
            int start = reqMap.indexOf("url")+6;
            int end = start+reqMap.substring(start).indexOf(",")-2;
            resultMap.put("url",reqMap.substring(start,end));
        }
        return resultMap;
    }

}
