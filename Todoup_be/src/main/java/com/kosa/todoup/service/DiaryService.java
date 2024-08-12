package com.kosa.todoup.service;

import com.kosa.todoup.dto.DiaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosa.todoup.mapper.DiaryMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    private final String uploadDir = "C:/uploads/";  // 절대경로 지정


    public void insertDiary(DiaryDTO diary, MultipartFile imgFile) throws IOException {
        if(imgFile != null && !imgFile.isEmpty()) {
            // imgFile이 있으면, 파일을 저장하고, 파일 경로를 diary 객체에 저장
            String newFileName = saveFile(imgFile);
            diary.setImgUrl(newFileName);
        } else {
            // imgFile이 없으면, 이미지 경로를 null로 설정
            diary.setImgUrl(null);
        }
        System.out.println("service : 완성된 diary => " + diary.toString());

        int result = diaryMapper.insertDiary(diary);

        System.out.println("데이터 저장 성공 : " + result);
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
        String newFileName = timeStamp + "_" + originalFileName + "." + fileExtension;

        // 파일의 경로를 가리키는 객체 생성(저장 디렉토리 + 파일 이름)
        Path filePath = Paths.get(uploadDir + newFileName);  // 경로를 가지고 와 Path 객체 생성

        // 저장할 디렉토리(폴더) 확인
        // createDirectories: 디렉토리가 존재하지 않는 경우, filePath 객체의 상위 디렉토리를 자동으로 생성
        Files.createDirectories(filePath.getParent());


        // 디버그 로그 출력
        System.out.println("Original File Name: " + originalFileName);
        System.out.println("File Extension: " + fileExtension);
        System.out.println("New File Name: " + newFileName);
        System.out.println("File Path: " + filePath);

        // 지정된 경로에 파일 데이터를 작성(업로드)
        Files.write(filePath, file.getBytes());

        // 새로운 파일이름을 반환
        return newFileName;
    }
}
