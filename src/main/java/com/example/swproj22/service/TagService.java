package com.example.swproj22.service;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.Tag;
import com.example.swproj22.repository.IssueJpaRepository;
import com.example.swproj22.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final IssueJpaRepository issueRepository;

    @Autowired
    public TagService(TagRepository tagRepository, IssueJpaRepository issueRepository) {
        this.tagRepository = tagRepository;
        this.issueRepository = issueRepository;
    }

    /*public void addTagToIssue(Long issueId, Long tagId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new RuntimeException("Issue not found"));
        Tag tag = tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found"));

        if (!issue.getTags().contains(tag)) {
            issue.getTags().add(tag);
            issueRepository.save(issue);
        }

    }*/

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
}
