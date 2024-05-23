package com.example.swproj22.service;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.Tag;
import com.example.swproj22.dto.TagCreateRequest;
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

    public Tag createTag(TagCreateRequest createRequest) {
        Tag tag = new Tag();
        tag.setCategory(createRequest.getCategory());
        return tagRepository.save(tag);
    }

    public void addTagToIssue() {
        List<Tag> allTags = tagRepository.findAll();
        List<Issue> allIssues = issueRepository.findAll();

        for(Issue issue : allIssues){
            issue.getTags().clear();
            issue.getTags().addAll(allTags);
            issueRepository.save(issue);
        }

    }
}
