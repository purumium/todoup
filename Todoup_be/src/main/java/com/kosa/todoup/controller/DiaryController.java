package com.kosa.todoup.controller;

import com.kosa.todoup.dto.DiaryDTO;
import com.kosa.todoup.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    // formDate 데이터: @ModelAttribute로 diaryDTO값을 받고, 파일은 개별 param으로 받기
    @PostMapping("/insert")
    public String diaryDataInsert(
            @ModelAttribute DiaryDTO diary,
            @RequestParam(value="imgFile", required = false) MultipartFile imgFile
    ) {

        System.out.println(diary.toString());
        System.out.println(imgFile);

        // user 연결되면 바꿔서 사용
        int userId = 1;
        diary.setUserId(userId);

        try {
            diaryService.insertDiary(diary, imgFile);

            return "오늘의 일기 추가 성공!";
        } catch (Exception e) {
            return "diary insert error";
        }
    }

    // 8월에 있는 모든 mood를 가지고 와야 함
    @GetMapping("/mood")
    public String getEmotionsByMonth() {
        // 필요한 정보 : userid, month 정보 => 날짜 데이터, emotion 가져오기
        return "";
    }

    @GetMapping("/emotion")
    public List<Mood> getmood() {
        List<Mood> moods = new ArrayList<>();

        // 예시 데이터, 실제로는 DB에서 데이터를 가져오도록
        moods.add(new Mood("2024-08-15", "happy"));
        moods.add(new Mood("2024-08-13", "sad"));
        moods.add(new Mood("2024-08-23", "exhaust"));
        moods.add(new Mood("2024-08-20", "angry"));
        return moods;
    }

    public static class Mood {
        private String date;
        private String mood;

        public Mood(String date, String mood) {
            this.date = date;
            this.mood = mood;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMood() {
            return mood;
        }

        public void setMood(String mood) {
            this.mood = mood;
        }
    }


}
