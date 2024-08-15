package com.kosa.todoup.controller;

import com.kosa.todoup.dto.GuestbookDTO;
import com.kosa.todoup.service.GuestbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<GuestbookDTO>> findRecentByOwner(
            @PathVariable Long ownerId,
            @RequestParam(defaultValue = "30") int daysAgo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<GuestbookDTO> guestbooks = guestbookService.findRecentByOwner(ownerId, daysAgo, page, size);
        if (guestbooks.isEmpty()) {
            return ResponseEntity.noContent().build();  // 데이터가 없으면 204 반환
        }
        return ResponseEntity.ok(guestbooks);  // 정상적으로 데이터를 반환
    }

    // 특정 사용자의 방명록 중, 특정 작성자가 남긴 방명록 조회
    @GetMapping("/users/{ownerId}/writers/{writerId}")
    public ResponseEntity<List<GuestbookDTO>> findByOwnerAndWriter(
            @PathVariable Long ownerId,
            @PathVariable Long writerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size) {
        List<GuestbookDTO> guestbooks = guestbookService.findByOwnerAndWriter(ownerId, writerId, page, size);
        if (guestbooks.isEmpty()) {
            return ResponseEntity.noContent().build();  // 데이터가 없으면 204 반환
        }
        return ResponseEntity.ok(guestbooks);  // 정상적으로 데이터를 반환
    }

    // 방명록 작성
    @PostMapping("/users/{ownerId}/writers/{writerId}")
    public ResponseEntity<Void> createGuestbook(
//            @PathVariable Long ownerId,
//            @PathVariable Long writerId,
            @RequestBody GuestbookDTO guestbookDTO) {
        int insertedRows = guestbookService.createGuestbook(guestbookDTO);
        if (insertedRows > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).build();  // 201 Created 반환
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // 500 Internal Server Error 반환
        }
    }

    // 방명록 수정
    @PutMapping("/{guestbookId}/writers/{writerId}")
    public ResponseEntity<Void> updateGuestbook(
//            @PathVariable Long guestbookId,
//            @PathVariable Long writerId,
            @RequestBody GuestbookDTO guestbookDTO) {
        int updatedRows = guestbookService.updateGuestbook(guestbookDTO);
        if (updatedRows > 0) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    // 방명록 삭제 (userId 기준)
    @DeleteMapping("/{guestbookId}/users/{userId}")
    public ResponseEntity<Void> deleteGuestbookByUserId(
            @PathVariable Long guestbookId,
            @PathVariable Long userId) {
        int deletedRows = guestbookService.deleteGuestbookByUserId(guestbookId, userId);
        if (deletedRows > 0) {
            return ResponseEntity.noContent().build();  // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found
        }
    }

    // 방명록 삭제 (writerId 기준)
    @DeleteMapping("/{guestbookId}/writers/{writerId}")
    public ResponseEntity<Void> deleteGuestbookByWriterId(
            @PathVariable Long guestbookId,
            @PathVariable Long writerId) {
        int deletedRows = guestbookService.deleteGuestbookByWriterId(guestbookId, writerId);
        if (deletedRows > 0) {
            return ResponseEntity.noContent().build();  // 204 No Content
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // 404 Not Found
        }
    }
}

