package com.lpf.ecs_springboot.controller;
import com.lpf.ecs_springboot.common.R;
import com.lpf.ecs_springboot.entity.Note;
import com.lpf.ecs_springboot.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/note")
public class NoteController {
    @Resource
    com.lpf.ecs_springboot.service.MysqlOpraterService MysqlOpraterService;
    @Resource
    com.lpf.ecs_springboot.service.NoteService NoteService;

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/CreteTable")
    public R CreteTable(@RequestBody Map param) {
        String date= String.valueOf(param.get("date"));
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int id = user.getId();
            String tabel_name = id+"_"+date;
            if(MysqlOpraterService.existTable(tabel_name) == 0 )
            {
                if(MysqlOpraterService.createNewTable(tabel_name) > 0 )
                {
                    R.ok();
                }
            }
            else {
                return R.error("已存在该账簿");
            }
            return R.ok();
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/getTableListById")
    public R getTableListById(@RequestBody Map param) {
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int id = user.getId();
            List<String> result = MysqlOpraterService.getTableListById(id);
            Map<String,Object> map=new HashMap<>();
            map.put("data",result);
            return R.ok(map);
        }
        else {
            return R.error("未登录");
        }
    }


    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/getNote")
    public R getNote(@RequestBody Map param) {
        String tableName= String.valueOf(param.get("tableName"));
        System.out.println(tableName);
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            List<Note> result = NoteService.getNote(tableName);
            Map<String,Object> map=new HashMap<>();
            map.put("data",result);
            return R.ok(map);
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/getNoteByID")
    public R getNoteByID(@RequestBody Map param) {
        String tableName= String.valueOf(param.get("tableName"));
        String id= String.valueOf(param.get("id"));
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();
            Note result = NoteService.getNoteByID(uid+"_"+tableName,id);
            Map<String,Object> map=new HashMap<>();
            map.put("data",result);
            return R.ok(map);
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/delNoteByID")
    public R delNoteByID(@RequestBody Map param) {
        String tableName= String.valueOf(param.get("tableName"));
        String id= String.valueOf(param.get("id"));
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int uid = user.getId();
            tableName = uid+"_"+tableName;
            int result = NoteService.delNoteByID(tableName,id);
            if(result>0)
            {
                return R.ok();
            }
            else {
                return R.error("删除失败");
            }
        }
        else {
            return R.error("未登录");
        }
    }

    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/addNote")
    public R addNote(@RequestBody Map param) {
        String date= String.valueOf(param.get("date"));
        HashMap<String, Object> map=(HashMap<String, Object>)  param.get("note");
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int id = user.getId();
            String tabel_name = id+"_"+date;
            map.put("tableName",tabel_name);
            int result = 0 ;
            if(MysqlOpraterService.existTable(tabel_name) == 0 )
            {
//                if(MysqlOpraterService.createNewTable(tabel_name) > 0 )
//                {
//                    System.out.println("创建表："+tabel_name);
//                }
//                else {
//                    return R.error("不存在表且创建失败");
//                }
                System.out.println("创建表："+MysqlOpraterService.createNewTable(tabel_name));
            }

            result = NoteService.addNote(map);

            if(result>0)
            {
                return R.ok();
            }
            else {
                return R.error("添加失败");
            }
        }
        else {
            return R.error("未登录");
        }
    }
    @CrossOrigin
    @ResponseBody
    @RequestMapping(value = "/updateNote")
    public R updateNote(@RequestBody Map param) {
        String date= String.valueOf(param.get("date"));
        HashMap<String, Object> map=(HashMap<String, Object>)  param.get("note");
        Object principal = SecurityUtils.getSubject().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            int id = user.getId();
            String tabel_name = id+"_"+date;
            map.put("tableName",tabel_name);
            int result = NoteService.updateNote(map);
            if(result>0)
            {
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

}

