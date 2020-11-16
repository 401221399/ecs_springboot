package com.lpf.ecs_springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lpf.ecs_springboot.common.R;
import com.lpf.ecs_springboot.entity.Cartoon;
import com.lpf.ecs_springboot.entity.Novel;
import com.lpf.ecs_springboot.entity.User;
import com.lpf.ecs_springboot.entity.UserInfo;
import com.lpf.ecs_springboot.service.NoteService;
import com.lpf.ecs_springboot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/userinfo")
public class UserInfoController {
    @Resource
    com.lpf.ecs_springboot.service.UserInfoService UserInfoService;

    @Resource
    com.lpf.ecs_springboot.service.NovelService NovelService;

    @Resource
    com.lpf.ecs_springboot.service.CartoonService cartoonService;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/getUserInfo")
    public R getUserInfo(@RequestBody Map param) {
        String date= String.valueOf(param.get("date"));
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();
            QueryWrapper qw = new QueryWrapper<UserInfo>();
            qw.eq("time",date);
            qw.eq("uid",uid);
            UserInfo user_info = UserInfoService.getOne(qw);
            Map<String,Object> map=new HashMap<>();
            map.put("data",user_info);
            return R.ok(map);
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/addUserInfo")
    public R addUserInfo(@RequestBody Map param) {
        UserInfo info = new UserInfo();
        info.setTime(String.valueOf(param.get("time")));
        info.setWeixin(String.valueOf(param.get("weixin")));
        info.setZhifubao(String.valueOf(param.get("zhifubao")));

        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();
            info.setUid(uid);
            boolean result = UserInfoService.save(info);
            if(result) {
                return R.ok();
            }
            else {
                return R.error("保存失败");
            }

        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/updateUserInfo")
    public R updateUserInfo(@RequestBody Map param) {
        UserInfo info = new UserInfo();
        info.setTime(String.valueOf(param.get("time")));
        info.setWeixin(String.valueOf(param.get("weixin")));
        info.setZhifubao(String.valueOf(param.get("zhifubao")));
        info.setId((Integer) param.get("id"));

        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();
            info.setUid(uid);
            boolean result = UserInfoService.updateById(info);
            if(result) {
                return R.ok();
            }
            else {
                return R.error("更新失败");
            }

        }
        else {
            return R.error("未登录");
        }
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/addNovel")
    public R addNovel(@RequestBody Map param) {
        Novel novel = new Novel();
        novel.setCatalogindex(param.get("catalogindex")+"");
        novel.setCatalogurl((String) param.get("catalogurl"));
        novel.setUrl((String) param.get("url"));
        novel.setName((String) param.get("name"));
        novel.setImg((String) param.get("img"));

        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();

            QueryWrapper qw = new QueryWrapper<UserInfo>();
            qw.eq("url",param.get("url"));
            qw.eq("uid",uid);
            if(NovelService.getOne(qw)!=null)
            {
                return R.error("已存在书架");
            }

            novel.setUid(uid);
            boolean result = NovelService.save(novel);
            if(result) {
                return R.ok();
            }
            else {
                return R.error("加入书架失败");
            }

        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/updateCatalog")
    public R updateNovel(@RequestBody Map param) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();

            QueryWrapper qw = new QueryWrapper<UserInfo>();
            qw.eq("url",param.get("url"));
            qw.eq("uid",uid);
            if(NovelService.getOne(qw)!=null)
            {
                Novel n = NovelService.getOne(qw);
                n.setCatalogindex(param.get("catalogindex")+"");
                n.setCatalogurl((String) param.get("catalogurl"));
                boolean result = NovelService.updateById(n);
                if(result) {
                    return R.ok();
                }
                else {
                    return R.error("更新失败");
                }
            }
            else {
                return R.error("该小说还未加入书架");
            }
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/getNovel")
    public R getNovel(@RequestBody Map param) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();
            QueryWrapper qw = new QueryWrapper<UserInfo>();
            qw.eq("uid",uid);
            List<Novel> list = NovelService.list(qw);
            try {
                Map resmap = new HashMap<String, Object>();
                resmap.put("list",list);
                return  R.ok(resmap);
            }
            catch (Exception e)
            {
                return R.error(e.getMessage());
            }
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/delNovel")
    public R delNovel(@RequestBody Map param) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        int id = (int) param.get("id");
        if (principal instanceof User) {
            QueryWrapper qw = new QueryWrapper<UserInfo>();
            qw.eq("id",id);
            boolean result = NovelService.removeById(id);
            if(result) {
                return R.ok();
            }
            else {
                return R.error("移除失败");
            }
        }
        else {
            return R.error("未登录");
        }
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/addCartoon")
    public R addCartoon(@RequestBody Map param) {
        Cartoon cartoon = new Cartoon();
        cartoon.setCatalogindex(param.get("catalogindex")+"");
        cartoon.setCatalogurl((String) param.get("catalogurl"));
        cartoon.setUrl((String) param.get("url"));
        cartoon.setName((String) param.get("name"));
        cartoon.setImg((String) param.get("img"));

        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();

            QueryWrapper qw = new QueryWrapper<UserInfo>();
            qw.eq("url",param.get("url"));
            qw.eq("uid",uid);
            if(cartoonService.getOne(qw)!=null)
            {
                return R.error("已存在书架");
            }

            cartoon.setUid(uid);
            boolean result = cartoonService.save(cartoon);
            if(result) {
                return R.ok();
            }
            else {
                return R.error("加入书架失败");
            }

        }
        else {
            return R.error("未登录");
        }
    }
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/getCartoon")
    public R getCartoon (@RequestBody Map param) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();
            QueryWrapper qw = new QueryWrapper<Cartoon>();
            qw.eq("uid",uid);
            List<Cartoon> list = cartoonService.list(qw);
            try {
                Map resmap = new HashMap<String, Object>();
                resmap.put("list",list);
                return  R.ok(resmap);
            }
            catch (Exception e)
            {
                return R.error(e.getMessage());
            }
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/delCartoon")
    public R delCartoon(@RequestBody Map param) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        int id = (int) param.get("id");
        if (principal instanceof User) {
            QueryWrapper qw = new QueryWrapper<Cartoon>();
            qw.eq("id",id);
            boolean result = cartoonService.removeById(id);
            if(result) {
                return R.ok();
            }
            else {
                return R.error("移除失败");
            }
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/updateCartoonCatalog")
    public R updateCartoonCatalog(@RequestBody Map param) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();

            QueryWrapper qw = new QueryWrapper<Cartoon>();
            qw.eq("url",param.get("url"));
            qw.eq("uid",uid);
            if(cartoonService.getOne(qw)!=null)
            {
                Cartoon n = cartoonService.getOne(qw);
                n.setCatalogindex(param.get("catalogindex")+"");
                n.setCatalogurl((String) param.get("catalogurl"));
                boolean result = cartoonService.updateById(n);
                if(result) {
                    return R.ok();
                }
                else {
                    return R.error("更新失败");
                }
            }
            else {
                return R.error("该小说还未加入书架");
            }
        }
        else {
            return R.error("未登录");
        }
    }
}
