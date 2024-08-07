package com.kosa.todoup.dao;

import com.kosa.todoup.dto.TodoDTO;

import java.sql.SQLException;

public interface TodoDAO {
    void insertTodo(TodoDTO todoDTO) throws SQLException;
}
