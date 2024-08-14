package com.kosa.todoup.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import java.util.Map;

@Mapper
public interface ReportMapper {

    // 1. 월별 획득 포인트(경험치)
    List<Map<String, Object>> findMonthlyPoints(@Param("userId") Long userId);

    // 2. 월별 todo 달성률
    List<Map<String, Object>> findMonthlyTodoCompletionRate(@Param("userId") Long userId);

    // 3. 카테고리별 할일 등록 비율
    List<Map<String, Object>> findCategoryRegistrationRatio(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);

    // 4. 카테고리별 할일 완료 비율
    List<Map<String, Object>> findCategoryCompletionRatio(@Param("userId") Long userId, @Param("yearMonth") String yearMonth);

    // 5. 월별 일기 작성 수
    List<Map<String, Object>> findMonthlyDiaryCount(@Param("userId") Long userId);
}
