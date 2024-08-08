package com.kosa.todoup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosa.todoup.mapper.DiaryMapper;

@Service
public class DiaryService {

    @Autowired
    private DiaryMapper diaryMapper;

}
