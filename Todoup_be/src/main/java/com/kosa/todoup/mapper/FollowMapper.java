package com.kosa.todoup.mapper;

import com.kosa.todoup.dto.FollowDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {

    // 팔로잉 리스트 불러오기
    List<FollowDTO> findFollowing(long userId);

    // 팔로우 리스트 불러오기
    List<FollowDTO> findFollowers(long userId);

    List<FollowDTO> findAllUsers(long userId);

    // 팔로잉 추가
    void insertFollow(long userId, long followId);

    // 팔로잉 삭제
    void deleteFollow(long userId, long followId);

    //팔로잉 확인
    boolean isFollowing(long userId, long followId);


    //////-------------///////////
    // 팔로우 추가
    void insertReceivedFollow(long userId, long followId);

    // 팔로우 삭제
    void deleteReceivedFollow(long userId, long followId);

    //팔로우 확인
    boolean isReceivedFollow(long userId, long followId);


}
