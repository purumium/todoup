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
            todoService.insertTodo(todoDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error creating todo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<TodoDTO>> getTodosByDate(
            @PathVariable String date) throws SQLException {
        long userId = 6;
        List<TodoDTO> todos = todoService.getTodosByDate(userId, date);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }

    @GetMapping("month/{date}")
    public ResponseEntity<List<TodoDTO>> getTodosByMonth(@PathVariable String date) throws SQLException {
        long userId = 6;
        List<TodoDTO> todos = todoService.getTodosByMonth(userId, date);
        return new ResponseEntity<>(todos, HttpStatus.OK);
    }
}
