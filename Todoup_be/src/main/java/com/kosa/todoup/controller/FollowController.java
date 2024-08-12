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

    @GetMapping("/followers")
    public ResponseEntity<List<FollowDTO>> findFollowers(@RequestParam long userId) {
        List<FollowDTO> followers = followService.findFollowers(userId);
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }
}
