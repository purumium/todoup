package com.kosa.todoup.mapper;

import com.kosa.todoup.dto.SignUpUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper

public interface UserMapper {
    void insertUser(SignUpUserDto params);

}
