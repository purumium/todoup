package com.kosa.todoup.service;

import com.kosa.todoup.dto.DiaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosa.todoup.mapper.DiaryMapper;

@Service
public class DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

    public void insertDiary(DiaryDTO diary) {

    }
}
