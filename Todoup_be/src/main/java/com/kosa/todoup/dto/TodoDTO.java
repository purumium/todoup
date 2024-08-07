package com.kosa.todoup.dto;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Data
@Getter
public class TodoDTO {
    @NotNull
    private long todoId;
    @NotNull
    private long userId;
    @NotNull
    private String title;
    private String memo;
    private boolean completed;
    @NotNull
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public TodoDTO(@NotNull long userId, @NotNull String title, String memo, @NotNull LocalDateTime startDate, LocalDateTime endDate) {
        this.userId = userId;
        this.title = title;
        this.memo = memo;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}