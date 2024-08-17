package com.kosa.todoup.service;

import com.kosa.todoup.dto.SignUpUserDto;
import com.kosa.todoup.dto.UserDTO;
import com.kosa.todoup.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Transactional
    public void insertUser(final SignUpUserDto params) {
        userMapper.insertUser(params);
    }

    public UserDTO loginService(UserDTO userDTO) {
        return userMapper.findUser(userDTO);
    }

    public Boolean checkEmail(String email) {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(email);
        return userMapper.findUser(userDTO) == null;
    }

    public Boolean checkNickname(String nickname) {
        UserDTO userDTO = new UserDTO();
        userDTO.setNickName(nickname);
        return userMapper.findUser(userDTO) == null;
    }
}
