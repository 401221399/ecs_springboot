package com.lpf.ecs_springboot.controller;

import com.lpf.ecs_springboot.common.CartoonCrowl_coco;
import com.lpf.ecs_springboot.common.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Map;
@Controller
@RequestMapping("/cartoon")
public class CartoonController {
    public static Logger logger = LoggerFactory.getLogger(CartoonController.class);
    private CartoonCrowl_coco cartoonCrowl;
    public CartoonController(){
        cartoonCrowl = new CartoonCrowl_coco();
    }

    @CrossOrigin
    @RequestMapping(value="/search",method= RequestMethod.POST)
    @ResponseBody
    public Map search(@RequestBody Map param){
        String word = String.valueOf(param.get("word"));
        String page = String.valueOf(param.get("page"));
        if (page == null)
        {
            page = "1";
        }
        return cartoonCrowl.Search(word,page);
    }

    @CrossOrigin
    @RequestMapping(value="/getCartoonAtrr")
    @ResponseBody
    public Map getCartoonAtrr(@RequestBody Map param){
        String url = String.valueOf(param.get("url"));
        return cartoonCrowl.getCartoonAtrr(url);
    }

    @CrossOrigin
    @RequestMapping(value="/getImgList")
    @ResponseBody
    public Map getImgList(@RequestBody Map param){
        String url = String.valueOf(param.get("url"));
        return cartoonCrowl.getImgList(url);
    }

    @CrossOrigin
    @RequestMapping(value="/getPic/{id}/{base64}/{page}",method= RequestMethod.GET)
    @ResponseBody
    public void getPic(@PathVariable("base64") String base64,@PathVariable("page") String page,@PathVariable("id") String id, HttpServletResponse response)throws Exception{
        String url = "https://img.cocomanhua.com/comic/"+id+"/"+base64+"/"+page;
        OutputStream out = response.getOutputStream();
        response.setStatus(HttpServletResponse.SC_OK);
        byte[] bytes = cartoonCrowl.getPic(url);
        out.write(bytes);
        out.close();
    }

    @CrossOrigin
    @RequestMapping(value="/getCover/{id}",method= RequestMethod.GET)
    @ResponseBody
    public void getPic(@PathVariable("id") String id, HttpServletResponse response)throws Exception{
        String url = "https://res.cocomanhua.com/comic/"+id+"/cover.jpg";
        OutputStream out = response.getOutputStream();
        response.setStatus(HttpServletResponse.SC_OK);
        byte[] bytes = cartoonCrowl.getPic(url);
        out.write(bytes);
        out.close();
    }

}
