package com.kosa.todoup.dto;

import lombok.Data;
import java.util.Date;

@Data
public class GuestbookDTO {
    private Long guestbookId;
    private Long userId;
    private Long writerId;
    private String content;
    private Date regDateAt;
}
