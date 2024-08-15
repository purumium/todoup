package com.kosa.todoup.dto;

import lombok.Data;

@Data
public class FollowDTO {
    private long userId;
    private long followId;
    private long userId1;
    private String userEmail;
    private String userNickname;
    private String userAvatarType;
    private String userPassword;
    private int lv;
    private int points;
    private String userCreatedAt;
    private long followUserId;
    private String followUserEmail;
    private String followNickname;
    private String followUserAvatarType;
    private String followUserPassword;
    private int followUserLv;
    private int followUserPoints;
    private String followUserCreatedAt;
    private String imgUrl;
}
