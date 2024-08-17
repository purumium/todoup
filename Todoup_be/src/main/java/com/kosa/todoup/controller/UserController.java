package com.kosa.todoup.controller;

import com.kosa.todoup.dto.SignUpUserDto;
import com.kosa.todoup.dto.UserDTO;
import com.kosa.todoup.service.UserService;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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

    @GetMapping("/login")
    public List<UserDTO> loginController(final @RequestParam("email") String email, final @RequestParam("password") String password) {
        UserDTO userdto = new UserDTO();
        userdto.setEmail(email);
        userdto.setPassword(password);
        return Arrays.asList(userService.loginService(userdto));
    }

    @GetMapping("/checkEmail")
    public Boolean checkEmail(@RequestParam("email") String email) {
        return userService.checkEmail(email);
    }

    @GetMapping("/checkNickname")
    public Boolean checkNickname(@RequestParam("nickname") String nickname) {
        return userService.checkNickname(nickname);
    }
}
