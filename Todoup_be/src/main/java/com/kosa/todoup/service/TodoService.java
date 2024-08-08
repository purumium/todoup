package com.kosa.todoup.service;

import com.kosa.todoup.dto.TodoDTO;
import com.kosa.todoup.mapper.TodoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class TodoService {

    @Autowired
    private TodoMapper todoMapper;

    public void insertTodo(TodoDTO todoDTO) throws SQLException {
        todoMapper.insertTodo(todoDTO);
    }
}
