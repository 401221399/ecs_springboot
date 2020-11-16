package com.lpf.ecs_springboot.dao;
import com.lpf.ecs_springboot.entity.Note;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

//用于进行数据库操作，建表，查表等
@Mapper
public interface NoteDao {
    List<Note> getNote(@Param("tableName")String tableName);
    int delNoteByID(@Param("tableName")String tableName,@Param("id")String id);
    int addNote(Map<String, Object> map);
    int updateNote(Map<String, Object> map);
    Note getNoteByID(@Param("tableName")String tableName,@Param("id")String id);

}
