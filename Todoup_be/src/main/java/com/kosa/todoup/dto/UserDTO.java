package com.kosa.todoup.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long userId;
    private String email;
    private String nickName;
    private String avatarType;
    private String password;
    private int lv;
    private int points;
    private LocalDate createdAt;
}
