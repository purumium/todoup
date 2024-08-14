package com.kosa.todoup.service;

import com.kosa.todoup.dto.GuestbookDTO;
import com.kosa.todoup.mapper.GuestbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookMapper guestbookMapper;

    @Autowired
    public GuestbookServiceImpl(GuestbookMapper guestbookMapper) {
        this.guestbookMapper = guestbookMapper;
    }

    @Override
    public List<GuestbookDTO> findRecentByOwner(Long ownerId, int daysAgo, int page, int size) {
        int offset = page * size;
        return guestbookMapper.findRecentByOwner(ownerId, daysAgo, offset, size);
    }

    @Override
    public List<GuestbookDTO> findByOwnerAndWriter(Long ownerId, Long writerId, int page, int size) {
        int offset = page * size;
        return guestbookMapper.findByOwnerAndWriter(ownerId, writerId, offset, size);
    }

    @Override
    public int createGuestbook(GuestbookDTO guestbookDTO) {
        return guestbookMapper.insertGuestbook(guestbookDTO);
    }

    @Override
    public int updateGuestbook(GuestbookDTO guestbookDTO) {
        return guestbookMapper.updateGuestbook(guestbookDTO);
    }

    @Override
    public int deleteGuestbookByUserId(Long guestbookId, Long userId) {
        return guestbookMapper.deleteGuestbookByUserId(guestbookId, userId);
    }

    @Override
    public int deleteGuestbookByWriterId(Long guestbookId, Long writerId) {
        return guestbookMapper.deleteGuestbookByWriterId(guestbookId, writerId);
    }
}
