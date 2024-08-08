package com.kosa.todoup.mapper;

import com.kosa.todoup.dto.TodoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TodoMapper {
    void insertTodo(TodoDTO todoDTO);

    List<TodoDTO> selectTodosByDate(@Param("date") String date);

    TodoDTO selectTodoById(@Param("todoId") long todoId);
}
