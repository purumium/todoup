package com.kosa.todoup.controller;

import com.kosa.todoup.dto.SignUpUserDto;
import com.kosa.todoup.service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUpUser(@RequestBody SignUpUserDto signUpUserDto) {
        System.err.println(signUpUserDto.toString());

        try {
            userService.insertUser(signUpUserDto);
            System.out.println(ResponseEntity.ok("회원가입이 완료되었습니다."));
            return ResponseEntity.ok("회원가입이 완료되었습니다.");

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
