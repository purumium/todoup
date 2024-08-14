package com.kosa.todoup.controller;

import com.kosa.todoup.dto.DiaryDTO;
import com.kosa.todoup.service.DiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.util.List;

@RestController
@RequestMapping("/diary")
public class DiaryController {

    @Autowired
    private DiaryService diaryService;

    @PostMapping("/insert")
    public String insertDiaryByDate( @ModelAttribute DiaryDTO diary,
                                     @RequestParam(value="imgFile", required = false) MultipartFile imgFile ) {
        // formDate: @ModelAttribute로 객체(diaryDTO)를 받고, 파일은 개별 param으로 받기
        System.out.println("insertDiaryByDate : " + diary.toString());
        System.out.println("insertDiaryByDate - img : " + imgFile);

        try {
            diaryService.insertDiaryByDate(diary, imgFile);

            return "today's diary insert success!";
        } catch (Exception e) {
            return "today's diary insert error 'ㅠ'";
        }
    }

    @GetMapping("/emotions/{yearMonth}")  // PathVariable: 경로에 변수 붙여서 사용
    public List<DiaryDTO> getEmotionByMonth(@PathVariable ("yearMonth") String yearMonth,
                                            @RequestParam ("userId") long userId) {
        List<DiaryDTO> emotionByMonth = diaryService.getEmotionByMonth(yearMonth, userId);

        return emotionByMonth;
    }


    @GetMapping("/detail/{date}")
    public DiaryDTO getDiaryByDate(@PathVariable("date") String date,
                                   @RequestParam ("userId") long userId) {
        System.out.println("getDiaryByDate - date로 다이어리 가져오기 : " + date);
        DiaryDTO diaryData = diaryService.getDiaryByDate(date, userId);

        return diaryData;
    }

    @DeleteMapping("/delete/{date}")  // @RequestMapping(value = "/delete/{diaryDate}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteDiaryByDate(@PathVariable("date") String date,
                                                    @RequestParam ("userId") long userId) {
        System.out.println("deleteDiaryByDate : " + date);
        try {
            diaryService.deleteDiaryByDate(date, userId);

            return ResponseEntity.ok(date + " 일기가 성공적으로 삭제!");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일기 삭제 중 오류 발생 'ㅠ'");
        }
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateDiaryByDate( @ModelAttribute DiaryDTO diary,
                                                     @RequestParam(value="imgFile", required = false) MultipartFile imgFile) {
        System.out.println("updateDiaryByDate : " + diary.toString());
        System.out.println(imgFile);

        try{
            diaryService.updateDiaryByDate(diary, imgFile);

            return ResponseEntity.ok("일기 수정 완료 :)");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("일기 수정 중 오류 발생 '0'");
        }
    }

    @GetMapping("/images/{imgData}")
    public ResponseEntity<Resource> getImage(@PathVariable("imgData") String imgFile) {
        try {
            Path filePath = Paths.get("C:/uploads/" + imgFile);
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch(MalformedURLException e){
            return ResponseEntity.badRequest().build();
        }
    }

}
