package com.kosa.todoup.service;

import com.kosa.todoup.dto.FollowDTO;
import com.kosa.todoup.mapper.FollowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowService {
    @Autowired
    private FollowMapper followMapper;

    public List<FollowDTO> findFollowing(long userId) {
        return followMapper.findFollowing(userId);
    }

    public List<FollowDTO> findFollowers(long userId) {
        return followMapper.findFollowers(userId);
    }
}
