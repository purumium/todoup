package com.kosa.todoup.controller;

import com.kosa.todoup.dto.GuestbookDTO;
import com.kosa.todoup.service.GuestbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/guestbooks")
public class GuestbookController {

    @Autowired
    private GuestbookService guestbookService;

    // 친구 아바타 방에서 사용자가 남긴 방명록 조회
    @GetMapping("/friend")
    public List<GuestbookDTO> findByOwnerAndWriter(
            @RequestParam Long ownerId,
            @RequestParam Long writerId) {
        return guestbookService.findByOwnerAndWriter(ownerId, writerId);
    }

    // 사용자의 아바타 방에서 친구들이 남긴 방명록 조회 (최근 daysAgo일 동안)
    @GetMapping("/recent")
    public List<GuestbookDTO> findRecentByOwner(
            @RequestParam Long ownerId,
            @RequestParam(defaultValue = "30") int daysAgo) {
        return guestbookService.findRecentByOwner(ownerId, daysAgo);
    }

    // 방명록 작성
    @PostMapping("/create")
    public void createGuestbook(@RequestBody GuestbookDTO guestbookDTO) {
        guestbookService.createGuestbook(guestbookDTO);
    }

    // 방명록 수정
    @PutMapping("/update")
    public void updateGuestbook(@RequestBody GuestbookDTO guestbookDTO) {
        guestbookService.updateGuestbook(guestbookDTO);
    }

    // 방명록 삭제 (userId 기준)
    @DeleteMapping("/deleteByUser")
    public void deleteGuestbookByUserId(@RequestParam Long guestbookId, @RequestParam Long userId) {
        guestbookService.deleteGuestbookByUserId(guestbookId, userId);
    }

    // 방명록 삭제 (writerId 기준)
    @DeleteMapping("/deleteByWriter")
    public void deleteGuestbookByWriterId(@RequestParam Long guestbookId, @RequestParam Long writerId) {
        guestbookService.deleteGuestbookByWriterId(guestbookId, writerId);
    }
}
