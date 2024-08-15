package com.kosa.todoup.dto;

import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Data
@Getter
public class TodoDTO {
    private long todo_id;
    @NotNull
    private long user_id;
    @NotNull
    private String title;
    private String memo;
    private boolean completed;
    private String category;
    @NotNull
    private String start_date;
    private String end_date;

    public TodoDTO(@NotNull long user_id, @NotNull String title, String memo, String category, @NotNull String start_date, String end_date) {
        this.user_id = user_id;
        this.title = title;
        this.memo = memo;
        this.category = category;
        this.start_date = start_date;
        this.end_date = end_date;
    }
}