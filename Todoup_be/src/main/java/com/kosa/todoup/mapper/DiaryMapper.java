package com.kosa.todoup.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.kosa.todoup.dto.DiaryDTO;

@Mapper
public interface DiaryMapper {
    public int insertDiary(DiaryDTO diary);
}

