package com.kosa.todoup.service;

import com.kosa.todoup.dto.TodoDTO;
import com.kosa.todoup.mapper.TodoMapper;
import com.kosa.todoup.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoMapper todoMapper;

    @Autowired
    private UserMapper userMapper;

    public List<TodoDTO> getTodosByDate(long userId, String date) {
        return todoMapper.selectTodosByDate(userId, date);
    }

    public List<TodoDTO> getTodosByMonth(long userId, String date) {
        return todoMapper.selectTodosByMonth(userId, date);
    }

    public void insertTodo(TodoDTO todoDTO) {
        todoMapper.insertTodo(todoDTO);
    }

    @Transactional
    public void toggleTodoCompletion(long todoId, int completed) {
        todoMapper.toggleTodoCompletion(todoId, completed); // 할 일의 완료 상태를 업데이트
        long userId = 6;
        int pointsChange = 0;

        if (completed == 1) {
            pointsChange = 5;
        } else {
            pointsChange = -5;
        }

        userMapper.updateUserPoints(userId, pointsChange); // 포인트 업데이트
        userMapper.updateUserLevel(userId); // 포인트와 레벨을 확인하여 레벨 업데이트
    }
}
