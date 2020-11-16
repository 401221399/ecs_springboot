package com.lpf.ecs_springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lpf.ecs_springboot.common.ShiroUtils;
import com.lpf.ecs_springboot.dao.MysqlOperaterDao;
import com.lpf.ecs_springboot.dao.NoteDao;
import com.lpf.ecs_springboot.dao.UserDao;
import com.lpf.ecs_springboot.entity.Note;
import com.lpf.ecs_springboot.entity.User;
import com.sun.tools.corba.se.idl.constExpr.Not;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class NoteService{
    @Resource
    NoteDao NoteDao;
    public List<Note> getNote(String tableName) {
        return NoteDao.getNote(tableName);
    }
    public Note getNoteByID(String tableName,String id) {
        return NoteDao.getNoteByID(tableName,id);
    }
    public int delNoteByID(String tableName,String id) {
        return NoteDao.delNoteByID(tableName,id);
    }

    public int addNote(Map<String, Object> map) {
        return NoteDao.addNote(map);
    }

    public int updateNote(Map<String, Object> map) {
        return NoteDao.updateNote(map);
    }


}
