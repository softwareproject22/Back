package com.example.swproj22.controller;

import com.example.swproj22.domain.Comment;
import com.example.swproj22.dto.commentAddRequest;
import com.example.swproj22.repository.commentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.swproj22.service.commentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class commentController {

    @Autowired
    private commentService commentService;

    @PostMapping("/add")
    public ResponseEntity<String> addComment(@RequestBody commentAddRequest request) {
        Comment comment = commentService.addComment(request);
        return ResponseEntity.status(HttpStatus.OK).body("코멘트가 추가되었습니다.");
    }
}
