package com.example.swproj22.controller;

import com.example.swproj22.domain.Tag;
import com.example.swproj22.dto.TagAddToIssueRequest;
import com.example.swproj22.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService){
        this.tagService = tagService;
    }

    @PostMapping("/addTagToIssue")
    public ResponseEntity<Void> assignTagToIssue(@RequestBody TagAddToIssueRequest request) {
        tagService.addTagToIssue(request.getIssueId(), request.getTagId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        return ResponseEntity.ok(tags);
    }
}
