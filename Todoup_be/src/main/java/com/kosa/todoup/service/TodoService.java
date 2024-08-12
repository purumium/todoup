package com.kosa.todoup.service;

import com.kosa.todoup.dto.TodoDTO;
import com.kosa.todoup.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoMapper todoMapper;

    public List<TodoDTO> getTodosByDate(long userId, String date) {
        return todoMapper.selectTodosByDate(userId, date);
    }

    public List<TodoDTO> getTodosByMonth(long userId, String date) {
        return todoMapper.selectTodosByMonth(userId, date);
    }

    public List<TodoDTO> getTodayList(long userId, Date date) {
        System.out.println("Service: "+todoMapper.selectTodosByDate(userId, String.valueOf(date)));
        return todoMapper.selectTodosByDate(userId, String.valueOf(date));
    }

    public void insertTodo(TodoDTO todoDTO) {
        todoMapper.insertTodo(todoDTO);
    }
}
