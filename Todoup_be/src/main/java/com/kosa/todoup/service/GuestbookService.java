package com.kosa.todoup.service;

import com.kosa.todoup.dto.GuestbookDTO;
import java.util.List;

public interface GuestbookService {

    // 특정 사용자의 아바타 방에서 친구들이 남긴 방명록 조회 (userId 기준, 최근 daysAgo일 동안의 기록)
    List<GuestbookDTO> findRecentByOwner(Long ownerId, int daysAgo);

    // 특정 사용자의 친구 아바타 방 방문 시 방명록 조회 (ownerId와 writerId 기준)
    List<GuestbookDTO> findByOwnerAndWriter(Long ownerId, Long writerId);

    // 새로운 방명록 항목 추가 (writerId 기준)
    void createGuestbook(GuestbookDTO guestbookDTO);

    // 방명록 항목 수정 (writerId 기준)
    void updateGuestbook(GuestbookDTO guestbookDTO);

    // 방명록 항목 삭제 (userId 기준)
    void deleteGuestbookByUserId(Long guestbookId, Long userId);

    // 방명록 항목 삭제 (writerId 기준)
    void deleteGuestbookByWriterId(Long guestbookId, Long writerId);
}
