package com.kosa.todoup.controller;

import com.kosa.todoup.dto.FollowDTO;
import com.kosa.todoup.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class FollowController {

    @Autowired
    private FollowService followService;

    @GetMapping("/following")
    public ResponseEntity<List<FollowDTO>> findFollowing(Long userId) {
        List<FollowDTO> following = followService.findFollowing(userId);
        return new ResponseEntity<>(following, HttpStatus.OK);
    }

    // 팔로잉 추가
    @PostMapping("/add/following")
    public ResponseEntity<String> addFollow(Long userId,  Long followId) {
        try {
            followService.insertFollow(userId, followId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error creating follow", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 팔로잉 삭제
    @DeleteMapping("/cancel/following")
    public ResponseEntity<Void> removeFollow(Long userId, Long followId) {
        followService.deleteFollow(userId, followId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 팔로잉 확인
    @GetMapping("/isFollowing")
    public ResponseEntity<Boolean> isFollowing(Long userId,  Long followId) {
        boolean result = followService.isFollowing(userId, followId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<FollowDTO>> findFollowers(Long userId) {
        List<FollowDTO> followers = followService.findFollowers(userId);
        System.out.println("findFollowers");
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    // 팔로우 추가
    @PostMapping("/add/followers")
    public ResponseEntity<Void> addFollowers(long userId,  long followId) {
        try {
            followService.insertReceivedFollow(userId, followId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 팔로우 삭제
    @DeleteMapping("/cancel/followers")
    public ResponseEntity<Void> removeFollowers(Long userId,  Long followId) {
        System.out.println("됨?");
        followService.deleteReceivedFollow(userId, followId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // 팔로우 확인
    @GetMapping("/isfollowers")
    public ResponseEntity<Boolean> isFollowers(Long userId,  Long followId) {
        boolean result = followService.isReceivedFollow(userId, followId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
