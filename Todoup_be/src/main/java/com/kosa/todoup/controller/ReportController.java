package com.kosa.todoup.controller;

import com.kosa.todoup.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // 1. 월별 획득 포인트(경험치)
    @GetMapping("/points")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyPoints(@RequestParam Long userId) {
        return ResponseEntity.ok(reportService.getMonthlyPoints(userId));
    }

    // 2. 월별 todo 달성률
    @GetMapping("/completion-rate")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyTodoCompletionRate(@RequestParam Long userId) {
        return ResponseEntity.ok(reportService.getMonthlyTodoCompletionRate(userId));
    }

    // 3. 카테고리별 할일 등록 비율
    @GetMapping("/category-registration-ratio")
    public ResponseEntity<List<Map<String, Object>>> getCategoryRegistrationRatio(@RequestParam Long userId,
                                                                                  @RequestParam String startDate,
                                                                                  @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getCategoryRegistrationRatio(userId, startDate, endDate));
    }

    // 4. 카테고리별 할일 완료 비율
    @GetMapping("/category-completion-ratio")
    public ResponseEntity<List<Map<String, Object>>> getCategoryCompletionRatio(@RequestParam Long userId,
                                                                                @RequestParam String startDate,
                                                                                @RequestParam String endDate) {
        return ResponseEntity.ok(reportService.getCategoryCompletionRatio(userId, startDate, endDate));
    }

    // 5. 월별 일기 작성 수
    @GetMapping("/diary-count")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyDiaryCount(@RequestParam Long userId) {
        return ResponseEntity.ok(reportService.getMonthlyDiaryCount(userId));
    }
}