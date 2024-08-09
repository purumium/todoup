package com.kosa.todoup.service;

import com.kosa.todoup.dto.TodoDTO;
import com.kosa.todoup.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoMapper todoMapper;

    public List<TodoDTO> getTodosByDate(long userid, String date) {
        return todoMapper.selectTodosByDate(userid, date);
    }

    public TodoDTO getTodoById(long todoId) {
        return todoMapper.selectTodoById(todoId);
    }

    public void insertTodo(TodoDTO todoDTO) {
        todoMapper.insertTodo(todoDTO);
    }
}
