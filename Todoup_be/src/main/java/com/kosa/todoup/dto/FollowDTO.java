package com.kosa.todoup.dto;

import lombok.Data;

@Data
public class FollowDTO {
    private long userId;
    private long followId;
    private String userNickname;
    private String followNickname;
    private int lv;
    private int points;
    private String imgUrl;
}
