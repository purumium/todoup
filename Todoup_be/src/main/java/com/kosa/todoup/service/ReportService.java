package com.kosa.todoup.service;

import java.util.List;
import java.util.Map;

public interface ReportService {

    // 1. 월별 획득 포인트(경험치)
    List<Map<String, Object>> getMonthlyPoints(Long userId);

    // 2. 월별 todo 달성률
    List<Map<String, Object>> getMonthlyTodoCompletionRate(Long userId);

    // 3. 카테고리별 할일 등록 비율
    List<Map<String, Object>> getCategoryRegistrationRatio(Long userId, String startDate, String endDate);

    // 4. 카테고리별 할일 완료 비율
    List<Map<String, Object>> getCategoryCompletionRatio(Long userId, String startDate, String endDate);

    // 5. 월별 일기 작성 수
    List<Map<String, Object>> getMonthlyDiaryCount(Long userId);
}
