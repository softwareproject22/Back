package com.example.swproj22.service;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.Tag;
import com.example.swproj22.domain.UserRole;
import com.example.swproj22.domain.entity.User;
import com.example.swproj22.dto.IssueCreateRequest;
import com.example.swproj22.dto.IssueEditCodeRequest;
import com.example.swproj22.dto.IssueMangerChangeRequest;
import com.example.swproj22.dto.IssueStatusChangeRequest;
import com.example.swproj22.repository.IssueJpaRepository;
import com.example.swproj22.repository.TagRepository;
import com.example.swproj22.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.function.Function;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IssueService {

    private final IssueJpaRepository issueJpaRepository;
    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    @Autowired
    public IssueService(IssueJpaRepository issueJpaRepository, TagRepository tagRepository, UserRepository userRepository) {
        this.issueJpaRepository = issueJpaRepository;
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
    }


    public Issue createIssue(IssueCreateRequest issueCreateRequest) {
        List<Tag> tags = issueCreateRequest.getTags().stream()
                .map(tagId -> tagRepository.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found with id: " + tagId)))
                .collect(Collectors.toList());

        Issue issue = Issue.builder()
                .projectId(issueCreateRequest.getProjectId())
                .title(issueCreateRequest.getTitle())
                .description(issueCreateRequest.getDescription())
                .code(issueCreateRequest.getCode())
                .status("new")
                .reporter(issueCreateRequest.getReporter())
                .tags(tags)
                .priority(issueCreateRequest.getPriority())
                .reportedTime(LocalDateTime.now())
                .build();

        return issueJpaRepository.save(issue);
    }
    public Issue getIssueByIssueId(Long issueId){
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));

        return issue;
    }
    public Issue editIssueCode(Long issueId, IssueEditCodeRequest editCodeRequest) {
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));

        issue.setCode(editCodeRequest.getCode());
        Issue savedIssue = issueJpaRepository.save(issue);
        if(savedIssue == null) {
            throw new RuntimeException("Failed to save issue with id: " + issueId);
        }
        return savedIssue;
        //return issueJpaRepository.save(issue);
    }

    public Issue changeIssueStatus(Long issueId, IssueStatusChangeRequest statusChangeRequest) {
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));
        User user = userRepository.findByNickname(statusChangeRequest.getNickname());
        UserRole useRole = user.getRole();      //user 구현되면 주석 해제
        String userRole = useRole.name();

        //String userRole = statusChangeRequest.getNickname();

        switch (statusChangeRequest.getStatus()) {
            case "fixed":
                if (!userRole.equals("DEV")) {
                    throw new IllegalStateException("Only users with 'dev' role can change status to 'fixed'.");
                }
                break;
            case "resolved":
                if (!userRole.equals("TESTER")) {
                    throw new IllegalStateException("Only users with 'tester' role can change status to 'resolved'.");
                }
                break;
            case "closed":
                if (!userRole.equals("PL")) {
                    throw new IllegalStateException("Only users with 'PL' role can change status to 'closed'.");
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid status: " + statusChangeRequest.getStatus());
        }


        issue.setStatus(statusChangeRequest.getStatus());
        return issueJpaRepository.save(issue);
    }

    public Issue changeIssueAssigned(Long issueId){
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));
        issue.setStatus("assigned");
        return issueJpaRepository.save(issue);
    }

    public Issue changeIssueAssignee(Long issueId, IssueMangerChangeRequest issueMangerChangeRequest) { //assignedUserId 는 지정될 이름

        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));

        User user = userRepository.findByNickname(issueMangerChangeRequest.getNickname());
        UserRole useRole = user.getRole();
        String userRole = useRole.name();
        //String userRole = issueMangerChangeRequest.getNickname(); //user구현되면 지울것

        if (!userRole.equals("PL")) {
            throw new IllegalStateException("Only users with 'PL' role can change assignee.");
        }

        issue.setAssignee(issueMangerChangeRequest.getAssignedUserId());
        return issueJpaRepository.save(issue);
    }

    public Issue changeIssueFixer(Long issueId){
        Issue issue = issueJpaRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException("Issue not found with id: " + issueId));
        String fixer = issue.getAssignee();
        issue.setFixer(fixer);
        return issueJpaRepository.save(issue);
    }

    public List<Issue> browse(Long projectId) {
        return issueJpaRepository.findByProjectId(projectId);
    }
    public List<Issue> searchByState(Long projectId, String state) {
        return issueJpaRepository.findByProjectIdAndStatus(projectId, state);
    }

    public List<Issue> getIssuesByProjectAndReporter(Long projectId, String reporter) {
        return issueJpaRepository.findByProjectIdAndReporter(projectId, reporter);
    }
    public List<Issue> getIssuesByProjectAndAssignee(Long projectId, String reporter) {
        return issueJpaRepository.findByProjectIdAndAssignee(projectId, reporter);
    }

    public Map<LocalDate, Long> countIssuesByDay(Long projectId){
        return issueJpaRepository.findByProjectId(projectId).stream()
                .collect(Collectors.groupingBy(
                        issue -> issue.getReportedTime().toLocalDate(),
                        Collectors.counting()
                ));
    }

}
