package com.kosa.todoup.service;

import com.kosa.todoup.dto.DiaryDTO;
import com.kosa.todoup.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosa.todoup.mapper.DiaryMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    @Autowired
    private UserMapper userMapper;

    private final String uploadDir = "C:/uploads/";  // 절대경로 지정

    @Transactional // 트랜잭션을 활성화
    public void insertDiaryByDate(DiaryDTO diary, MultipartFile imgFile) throws IOException {
        if(imgFile != null && !imgFile.isEmpty()) {
            String newFileName = saveFile(imgFile);  // imgFile이 있으면, 파일을 저장하고, 파일 경로를 diary 객체에 저장
            diary.setImageUrl(newFileName);
        } else {
            diary.setImageUrl(null); // imgFile이 없으면, 이미지 경로를 null로 설정
        }
        int result = diaryMapper.insertDiaryByDate(diary);

        // insert가 성공한 경우, 포인트와 레벨 업데이트
        int pointsChange = 0;
        if (result != 0) {
            pointsChange = 5;
        }
        userMapper.updateUserPoints(diary.getUserId(), pointsChange); // 포인트 업데이트
        userMapper.updateUserLevel(diary.getUserId()); // 포인트와 레벨을 확인하여 레벨 업데이트
    }

    public List<DiaryDTO> getEmotionByMonth(String yearMonth, long userId) {
        List<DiaryDTO> list = diaryMapper.getEmotionByMonth(yearMonth, userId);

        return list;
    }

    public DiaryDTO getDiaryByDate(String date, long userId) {
        DiaryDTO diaryData = diaryMapper.getDiaryByDate(date, userId);

        return diaryData;
    }

    @Transactional // 트랜잭션을 활성화
    public void deleteDiaryByDate(String date, long userId) {
        int result = diaryMapper.deleteDiaryByDate(date, userId);

        // delete가 성공한 경우, 포인트와 레벨 업데이트
        int pointsChange = 0;
        if (result != 0) {
            pointsChange = -5;
        }
        userMapper.updateUserPoints(userId, pointsChange); // 포인트 업데이트
        userMapper.updateUserLevel(userId); // 포인트와 레벨을 확인하여 레벨 업데이트
    }

    @Transactional // 트랜잭션을 활성화
    public void updateDiaryByDate(DiaryDTO diary, MultipartFile imgFile) throws IOException {
        if(imgFile != null && !imgFile.isEmpty()) {
            String newFileName = saveFile(imgFile);  // imgFile이 있으면, 파일을 저장하고, 파일 경로를 diary 객체에 저장
            diary.setImageUrl(newFileName);
        } else {
            diary.setImageUrl(null); // imgFile이 없으면, 이미지 경로를 null로 설정
        }
        
        diaryMapper.updateDiaryByDate(diary);
    }

    // 파일을 지정한 폴더에 저장 후, 새로운 파일 이름을 반환
    public String saveFile(MultipartFile file) throws IOException {
        // 현재의 날짜와 시간 포맷팅
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        // 원래 이름에서 확장자 분리
        // example.png => example와 .png를 분리
        String originalFileName = file.getOriginalFilename();
        String fileExtension = "";

        if(originalFileName != null && originalFileName.contains(".")) {
            // substring(int beginIndex, int endIndex)
            // lastIndexOf(String str) : str의 idx를 반환
            // "."의 위치를 찾아 확장자를 추출
            int dotIndex = originalFileName.lastIndexOf(".");

            fileExtension = originalFileName.substring(dotIndex);
            originalFileName = originalFileName.substring(0, dotIndex);
        }
        
        // 고유한 파일 이름 생성(중복 방지)
        String newFileName = timeStamp + "_" + originalFileName + fileExtension;

        // 파일의 경로를 가리키는 객체 생성(저장 디렉토리 + 파일 이름)
        Path filePath = Paths.get(uploadDir + newFileName);  // 경로를 가지고 와 Path 객체 생성

        // 저장할 디렉토리(폴더) 확인
        // createDirectories: 디렉토리가 존재하지 않는 경우, filePath 객체의 상위 디렉토리를 자동으로 생성
        Files.createDirectories(filePath.getParent());

        // 지정된 경로에 파일 데이터를 작성(업로드)
        Files.write(filePath, file.getBytes());

        // 새로운 파일이름을 반환
        return newFileName;
    }

}
