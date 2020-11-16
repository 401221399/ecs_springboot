package com.lpf.ecs_springboot.controller;

import cn.hutool.core.codec.Base64;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lpf.ecs_springboot.common.R;
import com.lpf.ecs_springboot.entity.User;
import com.lpf.ecs_springboot.entity.UserInfo;
import com.lpf.ecs_springboot.service.NovelService;
import com.lpf.ecs_springboot.service.UserService;
import net.dongliu.requests.RawResponse;
import net.dongliu.requests.Requests;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    @Resource
    com.lpf.ecs_springboot.service.UserService UserService;

    public static Logger logger = LoggerFactory.getLogger(NovelController.class);

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/login")
    public R login(@RequestBody Map param, HttpServletRequest request) {
        // 这里自己抛出自定义的异常信息
        try {
            String username= String.valueOf(param.get("username"));
            String password= String.valueOf(param.get("password"));
            System.out.println(username);
            Subject subject = SecurityUtils.getSubject();

            UsernamePasswordToken token = new UsernamePasswordToken(username, password);

            subject.login(token);
            Object principal = SecurityUtils.getSubject().getPrincipal();
            User user = (User) principal;
            HttpSession session = request.getSession();
            session.setAttribute("user",user);

        } catch (UnknownAccountException e) {
            return R.error("UnknownAccountException"); // 这个异常？？？弹到前台
        } catch (IncorrectCredentialsException e) {
            return R.error("账号或密码不正确");
        } catch (LockedAccountException e) {
            return R.error("账号已被锁定,请联系管理员");
        } catch (AuthenticationException e) {
            return R.error("账户验证失败");
        }

        return R.ok();
    }

    @CrossOrigin
    @RequestMapping(value = "/register")
    @ResponseBody
    public R save(@RequestBody Map param){
        String username= String.valueOf(param.get("username"));
        String password= String.valueOf(param.get("password"));
        String name= String.valueOf(param.get("name"));
        String openid= String.valueOf(param.get("openid"));
        User u = new User();
        u.setUsername(username);
        u.setPassword(password);
        u.setName(name);
        u.setOpenid(openid);

        if(UserService.getBaseMapper().selectOne(new QueryWrapper<User>().eq("username", username)) == null)
        {
            UserService.saveUser(u);
            return R.ok();
        }
        else {
            return R.error("已经存在用户");
        }

    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public R logout(HttpServletRequest request) {
        SecurityUtils.getSubject().logout();  //当前用户退出
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        return R.ok();
    }

    @ResponseBody
    @RequestMapping(value = "/authentication",method = RequestMethod.GET)
    @CrossOrigin
    public R authentication(HttpServletRequest request){
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            Map<String,Object> map=new HashMap<>();
            Map<String,Object> data=new HashMap<>();
            data.put("uid",user.getId());
            data.put("username",user.getUsername());
            data.put("name",user.getName());
            map.put("data",data);
            return R.ok(map);
        }
        return R.error("未登录");
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/WeChat_login", method = RequestMethod.POST)
    public R WeChat_login(@RequestBody Map param,HttpServletRequest request) {
        // 这里自己抛出自定义的异常信息
        String openid = String.valueOf(param.get("openid"));
        try {

            QueryWrapper qw = new QueryWrapper<UserInfo>();
            qw.eq("openid",openid);
            User u = UserService.getOne(qw);
            if (u==null)
            {
                return R.error("你的微信账号未在该小程序注册");
            }
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(u.getUsername(), u.getRepassword());
            subject.login(token);
            Object principal = SecurityUtils.getSubject().getPrincipal();
            User user = (User) principal;
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage()); // 这个异常？？？弹到前台
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/getWeChatSession/{code}", method = RequestMethod.GET)
    public R getWeChatSession(@PathVariable("code") String code) {
        // 这里自己抛出自定义的异常信息
        //System.out.println(code);
        try {
            String appid="wxefb5173acf1e7336";
            String secret="b47acaaae14be3a907dc6fec8c51d11d";
            String js_code=code;
            String url="https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type=authorization_code";
            RawResponse resp = Requests.get(url).send();
            //System.out.println(resp.readToText());
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode resNode = objectMapper.readTree(resp.readToText());
            String session = resNode.get("session_key").asText();
            String openid = resNode.get("openid").asText();

            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(openid.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8位字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            //一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方）
            String md5openid = new BigInteger(1, md.digest()).toString(16);
            Map<String,Object> map=new HashMap<>();
            map.put("openid",md5openid);
            map.put("session",session);
            logger.info("获取"+openid+"微信会话session："+session);
            return R.ok(map);
        } catch (Exception e) {
            return R.error(e.getMessage()); // 这个异常？？？弹到前台
        }
    }


}
