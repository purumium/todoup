package com.kosa.todoup.mapper;

import com.kosa.todoup.dto.GuestbookDTO;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface GuestbookMapper {

    // 특정 사용자의 아바타 방에서 친구들이 남긴 방명록 조회 (userId 기준, 최근 daysAgo일 동안의 기록)
    List<GuestbookDTO> findRecentByOwner(@Param("ownerId") Long ownerId, @Param("daysAgo") int daysAgo);

    // 친구 아바타 방 방문 시, 해당 사용자가 남긴 방명록 조회
    List<GuestbookDTO> findByOwnerAndWriter(@Param("ownerId") Long ownerId, @Param("writerId") Long writerId);

    // 새로운 방명록 항목 추가 (writerId 기준)
    @Options(useGeneratedKeys = true, keyProperty = "guestbookId")
    int insertGuestbook(GuestbookDTO guestbookDTO);

    // 방명록 항목 수정 (writerId 기준)
    int updateGuestbook(GuestbookDTO guestbookDTO);

    // 방명록 항목 삭제 (userId 기준)
    int deleteGuestbookByUserId(@Param("guestbookId") Long guestbookId, @Param("userId") Long userId);

    // 방명록 항목 삭제 (writerId 기준)
    int deleteGuestbookByWriterId(@Param("guestbookId") Long guestbookId, @Param("writerId") Long writerId);
}
