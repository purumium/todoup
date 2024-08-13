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
        System.out.println("올바르게 동작함?");
        return followMapper.findFollowing(userId);
    }

    public List<FollowDTO> findFollowers(long userId) {
        return followMapper.findFollowers(userId);
    }
    // 팔로잉 추가
    public void insertFollow(long userId, long followId) {
        followMapper.insertFollow(userId, followId);
    }

    // 팔로잉 삭제
    public void deleteFollow(long userId, long followId) {
        followMapper.deleteFollow(userId, followId);
    }

    //팔로잉 확인
    public boolean isFollowing(long userId, long followId) {
        return followMapper.isFollowing(userId, followId);
    }


    //팔로우 추가
    public  void insertReceivedFollow(long userId, long followId){
        followMapper.insertReceivedFollow(userId, followId);
    }
    
    //팔로우 삭제
    public void deleteReceivedFollow(long userId, long followId){
        followMapper.deleteReceivedFollow(userId, followId);
    }

    //팔로우 받았는지 확인
    public boolean isReceivedFollow(long userId, long followId){
        return followMapper.isReceivedFollow(userId, followId);
    }
}
