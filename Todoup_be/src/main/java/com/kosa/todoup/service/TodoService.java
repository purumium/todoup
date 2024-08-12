package com.kosa.todoup.service;

import com.kosa.todoup.dto.TodoDTO;
import com.kosa.todoup.mapper.TodoMapper;
import com.kosa.todoup.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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

    public List<TodoDTO> getTodayList(long userId, Date date) {
        System.out.println("Service: "+todoMapper.selectTodosByDate(userId, String.valueOf(date)));
        return todoMapper.selectTodosByDate(userId, String.valueOf(date));
    }

    public long insertTodo(TodoDTO todoDTO) {

        todoMapper.insertTodo(todoDTO);
        return todoDTO.getTodo_id();
    }


    @Transactional
    public void toggleTodoCompletion(long userId, long todoId, int completed) {
        todoMapper.toggleTodoCompletion(todoId, completed); // 할 일의 완료 상태를 업데이트
        int pointsChange = 0;

        if (completed == 1) {
            pointsChange = 5;
        } else {
            pointsChange = -5;
        }

        userMapper.updateUserPoints(userId, pointsChange); // 포인트 업데이트
        userMapper.updateUserLevel(userId); // 포인트와 레벨을 확인하여 레벨 업데이트
    }

    @Transactional
    public void deleteTodo(long userId, long todoId, int completed) {
        // 유저가 존재하는지 확인
        if (!userMapper.findUserById(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        // 투두 항목 삭제
        todoMapper.deleteTodoById(userId, todoId);

        // 만약 완료된 투두를 삭제했다면 포인트와 레벨 업데이트
        if (completed == 1) {
            userMapper.updateUserPointsOnTodoDelete(userId);
        }
    }
}
