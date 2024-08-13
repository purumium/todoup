package com.kosa.todoup.service;

import com.kosa.todoup.mapper.ReportMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportMapper reportMapper;

    @Autowired
    public ReportServiceImpl(ReportMapper reportMapper) {
        this.reportMapper = reportMapper;
    }

    @Override
    public List<Map<String, Object>> getMonthlyPoints(Long userId) {
        return reportMapper.findMonthlyPoints(userId);
    }

    @Override
    public List<Map<String, Object>> getMonthlyTodoCompletionRate(Long userId) {
        return reportMapper.findMonthlyTodoCompletionRate(userId);
    }

    @Override
    public List<Map<String, Object>> getCategoryRegistrationRatio(Long userId, String startDate, String endDate) {
        return reportMapper.findCategoryRegistrationRatio(userId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getCategoryCompletionRatio(Long userId, String startDate, String endDate) {
        return reportMapper.findCategoryCompletionRatio(userId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getMonthlyDiaryCount(Long userId) {
        return reportMapper.findMonthlyDiaryCount(userId);
    }
}

