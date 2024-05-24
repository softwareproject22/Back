package com.example.swproj22.controller;

import com.example.swproj22.domain.Tag;
import com.example.swproj22.domain.Issue;
import com.example.swproj22.dto.TagCreateRequest;
import com.example.swproj22.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody TagCreateRequest createRequest) {
        Tag tag = tagService.createTag(createRequest);
        return ResponseEntity.ok(tag);
    }

    @PostMapping("/addTagToIssue")
    public ResponseEntity<Void> assignTagToIssue() {
        tagService.addTagToIssue();
        return ResponseEntity.ok().build();
    }

}
