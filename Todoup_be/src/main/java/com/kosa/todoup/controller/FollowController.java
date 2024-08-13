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
    public ResponseEntity<List<FollowDTO>> findFollowing(@RequestParam long userId) {
        List<FollowDTO> following = followService.findFollowing(userId);
        return new ResponseEntity<>(following, HttpStatus.OK);
    }
    // 팔로우 추가
    @PostMapping("/add/following")
    public ResponseEntity<Void> addFollow(@RequestParam long userId, @RequestParam long followId) {
        followService.insertFollow(userId, followId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    // 팔로우 삭제
    @DeleteMapping("/remove/following")
    public ResponseEntity<Void> removeFollow(@RequestParam long userId, @RequestParam long followId) {
        System.out.println(userId);
        System.out.println(followId);
        followService.deleteFollow(userId, followId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/followers")
    public ResponseEntity<List<FollowDTO>> findFollowers(@RequestParam long userId) {
        List<FollowDTO> followers = followService.findFollowers(userId);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }
}
