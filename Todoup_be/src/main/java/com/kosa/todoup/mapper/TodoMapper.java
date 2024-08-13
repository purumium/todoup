package com.kosa.todoup.mapper;

import com.kosa.todoup.dto.TodoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TodoMapper {
    void insertTodo(TodoDTO todoDTO);
    List<TodoDTO> selectTodosByDate(long userId, @Param("date") String date);
    List<TodoDTO> selectTodosByMonth(long userId, @Param("date") String date);
    void toggleTodoCompletion(@Param("todoId") long todoId, @Param("completed") int completed);
    void deleteTodoById(long userId, @Param("todoId") long todoId);

    void modifyTodobyTodoId(TodoDTO todoDTO);

}
