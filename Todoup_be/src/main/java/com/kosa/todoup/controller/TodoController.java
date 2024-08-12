package com.kosa.todoup.controller;

import com.kosa.todoup.dto.TodoDTO;
import com.kosa.todoup.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping("/insert")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO) {
        try {
            long createdTodoId = todoService.insertTodo(todoDTO);
            return new ResponseEntity<>(createdTodoId, HttpStatus.CREATED); // 생성된 todo_id 반환
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error creating todo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<TodoDTO>> getTodosByDate(@RequestParam("userId") long userId,
            @PathVariable String date) throws SQLException {
        List<TodoDTO> todos = todoService.getTodosByDate(userId, date);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @GetMapping("month/{date}")
    public ResponseEntity<List<TodoDTO>> getTodosByMonth(@RequestParam("userId") long userId, @PathVariable String date) throws SQLException {
        List<TodoDTO> todos = todoService.getTodosByMonth(userId, date);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @PostMapping("/completion/{todoId}")
    public ResponseEntity<?> toggleCompletion(@RequestParam("userId") long userId, @PathVariable long todoId, @RequestParam int completed) {
        try {
            todoService.toggleTodoCompletion(userId, todoId, completed);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error toggling completion", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
