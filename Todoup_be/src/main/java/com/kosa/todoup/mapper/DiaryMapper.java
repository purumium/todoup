package com.kosa.todoup.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.kosa.todoup.dto.DiaryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DiaryMapper {
    int insertDiaryByDate(DiaryDTO diary);

    List<DiaryDTO> getEmotionByMonth(@Param("yearMonth") String yearMonth, @Param("userId") long userId);

    DiaryDTO getDiaryByDate(@Param("diaryDate") String date, @Param("userId") long userId);

    int deleteDiaryByDate(@Param("diaryDate") String date, @Param("userId") long userId);

    void updateDiaryByDate(DiaryDTO diary);

    // @Param으로 넘어온 데이터는 지정한 변수명으로 mapper에서 사용 가능
}

