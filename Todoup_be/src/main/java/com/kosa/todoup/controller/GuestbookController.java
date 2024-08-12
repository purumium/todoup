package com.kosa.todoup.controller;

import com.kosa.todoup.dto.GuestbookDTO;
import com.kosa.todoup.service.GuestbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/guestbooks")
public class GuestbookController {
    private final GuestbookService guestbookService;

    @Autowired
    GuestbookController (GuestbookService guestbookService) {
        this.guestbookService = guestbookService;
    }


    // 특정 사용자의 방명록 중, 최근 작성된 방명록 조회
    @GetMapping("/users/{ownerId}/recent")
    public List<GuestbookDTO> findRecentByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "30") int daysAgo) {
        return guestbookService.findRecentByOwner(ownerId, daysAgo);
    }

    // 특정 사용자의 방명록 중, 특정 작성자가 남긴 방명록 조회
    @GetMapping("/users/{ownerId}/writers/{writerId}")
    public List<GuestbookDTO> findByOwnerAndWriter(
            @PathVariable Long ownerId,
            @PathVariable Long writerId) {
        return guestbookService.findByOwnerAndWriter(ownerId, writerId);
    }


    // 방명록 작성
    @PostMapping("/users/{ownerId}/writers/{writerId}")
    public void createGuestbook(
            @PathVariable Long ownerId,
            @PathVariable Long writerId,
            @RequestBody GuestbookDTO guestbookDTO) {
        guestbookService.createGuestbook(guestbookDTO);
    }

    // 방명록 수정
    @PutMapping("/{guestbookId}/writers/{writerId}")
    public void updateGuestbook(
            @PathVariable Long guestbookId,
            @PathVariable Long writerId,
            @RequestBody GuestbookDTO guestbookDTO) {
        guestbookService.updateGuestbook(guestbookDTO);
    }

    // 방명록 삭제 (userId 기준)
    @DeleteMapping("/{guestbookId}/users/{userId}")
    public void deleteGuestbookByUserId(
            @PathVariable Long guestbookId,
            @PathVariable Long userId) {
        guestbookService.deleteGuestbookByUserId(guestbookId, userId);
    }

    // 방명록 삭제 (writerId 기준)
    @DeleteMapping("/{guestbookId}/writers/{writerId}")
    public void deleteGuestbookByWriterId(
            @PathVariable Long guestbookId,
            @PathVariable Long writerId) {
        guestbookService.deleteGuestbookByWriterId(guestbookId, writerId);
    }
}

