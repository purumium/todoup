package com.kosa.todoup.controller;

import com.kosa.todoup.dto.TodoDTO;
import com.kosa.todoup.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
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


    @GetMapping("/today")
    public List<TodoDTO> getTodayList(@RequestParam long userId) throws SQLException {
        System.err.println("controller : "+userId);
        Date date = Date.valueOf(LocalDate.now());
        System.err.println(todoService.getTodayList(userId, date));
        return todoService.getTodayList(userId, date);
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


    @DeleteMapping("/delete/{todoId}")
    public ResponseEntity<?> deleteTodo(@RequestParam("userId") long userId, @PathVariable long todoId, @RequestParam("completed") int completed) {
        try {
            todoService.deleteTodo(userId, todoId, completed);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error deleting todo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/modify/{todoId}")
    public ResponseEntity<?> updateTodo(@PathVariable("todoId") long todo_id, @RequestBody TodoDTO todoDTO) {
        todoDTO.setTodo_id(todo_id);
        try {
            todoService.updateTodo(todoDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error deleting todo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
