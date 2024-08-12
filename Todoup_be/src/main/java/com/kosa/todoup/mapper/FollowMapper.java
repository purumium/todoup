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

    // 팔로우 추가
    void insertFollow(long userId, long followId);

    // 팔로우 삭제
    void deleteFollow(long userId, long followId);
}
