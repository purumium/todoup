package com.kosa.todoup.mapper;

import com.kosa.todoup.dto.FollowDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FollowMapper {
    List<FollowDTO> findFollowing(long userId);
    List<FollowDTO> findFollowers(long userId);
}
