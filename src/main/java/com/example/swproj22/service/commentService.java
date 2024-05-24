package com.example.swproj22.service;

import com.example.swproj22.domain.Comment;
import com.example.swproj22.domain.Issue;
import com.example.swproj22.dto.commentAddRequest;
import com.example.swproj22.repository.IssueJpaRepository;
import com.example.swproj22.repository.commentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.swproj22.repository.commentJpaRepository;

@Service
public class commentService {

    private final commentJpaRepository commentJpaRepository;
    private final IssueJpaRepository issueJpaRepository;

    @Autowired
    public commentService(commentJpaRepository commentJpaRepository, IssueJpaRepository issueJpaRepository) {
        this.commentJpaRepository = commentJpaRepository;
        this.issueJpaRepository = issueJpaRepository;
    }

    public Comment addComment(commentAddRequest commentAddRequest) {
        Comment comment = Comment.builder()
                .content(commentAddRequest.getContent())
                .issue(issueJpaRepository.findById(commentAddRequest.getIssueId()).orElseThrow(() -> new RuntimeException("Issue not found")))
                .sender(commentAddRequest.getSender())
                .build();

        return commentJpaRepository.save(comment);
    }

}
