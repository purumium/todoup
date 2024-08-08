package com.kosa.todoup.mapper;

import com.kosa.todoup.dto.TodoDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TodoMapper {
    void insertTodo(TodoDTO todoDTO);
}
