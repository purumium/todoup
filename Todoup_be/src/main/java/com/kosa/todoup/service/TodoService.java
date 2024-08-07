package com.kosa.todoup.service;

import com.kosa.todoup.dao.TodoDAO;
import com.kosa.todoup.dto.TodoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class TodoService {

    @Autowired
    private TodoDAO todoDAO;

    public void insertTodo(TodoDTO todoDTO) throws SQLException {
        todoDAO.insertTodo(todoDTO);
    }
}
