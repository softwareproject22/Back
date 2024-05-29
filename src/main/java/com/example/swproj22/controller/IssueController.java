package com.example.swproj22.controller;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.dto.IssueCreateRequest;
import com.example.swproj22.dto.IssueEditCodeRequest;
import com.example.swproj22.dto.IssueMangerChangeRequest;
import com.example.swproj22.dto.IssueStatusChangeRequest;
import com.example.swproj22.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/issue")
public class IssueController {

    private final IssueService issueService;

    @Autowired
    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    public ResponseEntity<String> createIssue(@RequestBody IssueCreateRequest issueCreateRequest) {
        try {
            Issue createdIssue = issueService.createIssue(issueCreateRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body("이슈가 생성되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이슈 생성에 실패하였습니다.");
        }
    }

    @GetMapping("/{issueId}")
    public ResponseEntity<Issue> getIssueByIssueId(@PathVariable Long issueId) {
        try {
            Issue issue = issueService.getIssueByIssueId(issueId);
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/editCode/{issueId}")
    public ResponseEntity<String> editIssueCode(@PathVariable Long issueId, @RequestBody IssueEditCodeRequest editCodeRequest) {
        try {
            issueService.editIssueCode(issueId, editCodeRequest);
            return ResponseEntity.status(HttpStatus.OK).body("코드가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("코드 수정에 실패하였습니다.");
        }
    }

    @PutMapping("/changeStatus/{issueId}")
    public ResponseEntity<String> changeIssueStatus(@PathVariable Long issueId, @RequestBody IssueStatusChangeRequest statusChangeRequest) {
        try {
            Issue changedIssue = issueService.changeIssueStatus(issueId, statusChangeRequest);
            return ResponseEntity.status(HttpStatus.OK).body("이슈의 상태가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이슈 상태 변경에 실패하였습니다.");
        }
    }

    @PutMapping("/changeAssigned/{issueId}")
    public ResponseEntity<String> changeIssueAssigned(@PathVariable Long issueId) {
        try {
            Issue changedIssue = issueService.changeIssueAssigned(issueId);
            return ResponseEntity.status(HttpStatus.OK).body("이슈 상태가 assigned 되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("assigned로 상태변경에 실패하였습니다.");
        }
    }

    @PutMapping("/changeAssignee/{issueId}")
    public ResponseEntity<String> changeIssueAssignee(@PathVariable Long issueId, @RequestBody IssueMangerChangeRequest issueMangerChangeRequest) {
        try {
            Issue changedIssue = issueService.changeIssueAssignee(issueId, issueMangerChangeRequest);
            return ResponseEntity.status(HttpStatus.OK).body("이슈의 assignee가 성공적으로 배정되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이슈 담당자 배정에 실패하였습니다.");
        }
    }

    @PutMapping("/changeFixer/{issueId}")
    public ResponseEntity<String> changeIssueFixer(@PathVariable Long issueId) {
        try {
            Issue changedIssue = issueService.changeIssueFixer(issueId);
            return ResponseEntity.status(HttpStatus.OK).body("이슈의 fixer가 성공적으로 변경되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이슈 fixer 변경에 실패하였습니다.");
        }
    }

    @GetMapping("/browse/{projectId}")
    public ResponseEntity<List<Issue>> browse(@PathVariable Long projectId) {
        try {
            List<Issue> issues = issueService.browse(projectId);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/searchByState/{projectId}/{state}")
    public ResponseEntity<List<Issue>> searchByState(@PathVariable Long projectId, @PathVariable String state) {
        try {
            List<Issue> issues = issueService.searchByState(projectId, state);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/searchByReporter/{projectId}/{reporter}")
    public ResponseEntity<List<Issue>> getIssuesByProjectAndReporter(@PathVariable Long projectId, @PathVariable String reporter) {
        try {
            List<Issue> issues = issueService.getIssuesByProjectAndReporter(projectId, reporter);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/searchByAssignee/{projectId}/{assignee}")
    public ResponseEntity<List<Issue>> getIssuesByProjectAndAssignee(@PathVariable Long projectId, @PathVariable String assignee) {
        try {
            List<Issue> issues = issueService.getIssuesByProjectAndAssignee(projectId, assignee);
            return ResponseEntity.ok(issues);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
