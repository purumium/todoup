package com.kosa.todoup.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class DiaryDTO {
    private int diaryId;        // 일기 ID
    private int userId;         // 사용자 ID
    private Date diaryDate; // 일기의 내용이 해당하는 날짜
    private String emotion;      // 감정 상태
    private String weather;      // 날씨
    private String content;      // 일기 내용
    private String imageUrl;       // 이미지 URL
    private Date writtenAt; // 일기를 실제로 작성한 날짜
}
