package com.kosa.todoup.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignUpUserDto {
    private long userId;
    private String email;
    private String password;
    private String nickName;
}
