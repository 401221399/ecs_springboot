package com.lpf.ecs_springboot.controller;;

import com.lpf.ecs_springboot.common.BqgCrowl;
import com.lpf.ecs_springboot.common.NovelCrowl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/novel")
public class NovelController {
    public static Logger logger = LoggerFactory.getLogger(NovelController.class);
//    private NovelCrowl novelCrowl;
//    public NovelController(){
////        novelCrowl = new NovelCrowl();
////    }
    private BqgCrowl novelCrowl;
    public NovelController(){
        novelCrowl = new BqgCrowl();
    }

    @CrossOrigin
    @RequestMapping(value="/search",method= RequestMethod.GET)
    @ResponseBody
    public Map search(HttpServletRequest request)throws Exception{
        logger.info(request.getRemoteAddr());
        logger.info(request.getRemotePort()+"");
        String word = request.getParameter("word");
        return novelCrowl.Search(word);
    }

    @CrossOrigin
    @RequestMapping(value="/getNovelAttr")
    @ResponseBody
    public Map getNovelAttr(@RequestBody Map param)throws Exception{
        String url = String.valueOf(param.get("url"));
        return novelCrowl.getNovelAtrr(url);
    }

    @CrossOrigin
    @RequestMapping(value="/openCatalog")
    @ResponseBody
    public Map openCatalog(@RequestBody Map param)throws Exception{
        String url = String.valueOf(param.get("url"));
        return novelCrowl.openCatalog(url);
    }
}
