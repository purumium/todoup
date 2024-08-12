package com.kosa.todoup.service;

import com.kosa.todoup.dto.FollowDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FollowService {

    public List<FollowDTO> findFollowing(long userId) {
        FollowDTO followDTO = new FollowDTO();
        followDTO.setUserId(1);
        followDTO.setFollowId(1);
        return Arrays.asList(followDTO);
    }

    public List<FollowDTO> findFollowers(long userId){
        FollowDTO followDTO = new FollowDTO();
        followDTO.setUserId(1);
        followDTO.setFollowId(1);
        return Arrays.asList(followDTO);
    }
}
