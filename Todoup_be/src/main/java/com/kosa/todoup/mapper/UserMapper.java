package com.kosa.todoup.mapper;

import com.kosa.todoup.dto.SignUpUserDto;
import com.kosa.todoup.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(SignUpUserDto params);
    UserDTO findUser(UserDTO userDTO);
    void updateUserPoints(long userId, int points);
    void updateUserLevel(long userId);
}
