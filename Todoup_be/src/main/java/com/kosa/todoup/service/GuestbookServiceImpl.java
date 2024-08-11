package com.kosa.todoup.service;

import com.kosa.todoup.dto.GuestbookDTO;
import com.kosa.todoup.mapper.GuestbookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GuestbookServiceImpl implements GuestbookService {

    @Autowired
    private GuestbookMapper guestbookMapper;

    @Override
    public List<GuestbookDTO> findByOwnerAndWriter(Long ownerId, Long writerId) {
        return guestbookMapper.findByOwnerAndWriter(ownerId, writerId);
    }

    @Override
    public List<GuestbookDTO> findRecentByOwner(Long ownerId, int daysAgo) {
        return guestbookMapper.findRecentByOwner(ownerId, daysAgo);
    }

    @Override
    public void createGuestbook(GuestbookDTO guestbookDTO) {
        guestbookMapper.insertGuestbook(guestbookDTO);
    }

    @Override
    public void updateGuestbook(GuestbookDTO guestbookDTO) {
        guestbookMapper.updateGuestbook(guestbookDTO);
    }

    @Override
    public void deleteGuestbookByUserId(Long guestbookId, Long userId) {
        guestbookMapper.deleteGuestbookByUserId(guestbookId, userId);
    }

    @Override
    public void deleteGuestbookByWriterId(Long guestbookId, Long writerId) {
        guestbookMapper.deleteGuestbookByWriterId(guestbookId, writerId);
    }
}
