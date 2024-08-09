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
            @RequestParam(value="imgUrl", required = false)MultipartFile imgUrl
    ) {
        System.out.println(diary.toString());
        // user 연결되면 바꿔서 사용
        int userId = 1;
        diary.setUserId(userId);

        try {
            if( imgUrl == null || imgUrl.isEmpty() ){
                diary.setImgUrl(null);  // imgUrl이 없으면 null로 설정
            }
            diaryService.insertDiary(diary);

            return "diary insert success";
        } catch (Exception e) {
            return "diary insert error";
        }
    }

    @GetMapping("/emotion")
    public List<Mood> getMoods() {
        List<Mood> moods = new ArrayList<>();

        // 예시 데이터, 실제로는 DB에서 데이터를 가져오도록 구현
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
