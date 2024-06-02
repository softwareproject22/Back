package com.example.swproj22.service;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.Tag;
import com.example.swproj22.domain.UserRole;
import com.example.swproj22.domain.entity.User;
import com.example.swproj22.repository.IssueJpaRepository;
import com.example.swproj22.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Objects;

@Service
public class RecommendService {

    private final IssueJpaRepository issueJpaRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecommendService(IssueJpaRepository issueJpaRepository, UserRepository userRepository){
        this.issueJpaRepository = issueJpaRepository;
        this.userRepository = userRepository;
    }

    public List<String> recommendAssigneesByTag(List<Tag> tags) {
        List<String> statuses = List.of("resolved", "closed"); 
        List<Issue> matchedIssues = issueJpaRepository.findByStatusesAndTags(statuses, tags);
        Set<String> busyAssignees = getBusyAssignees();

        if (!matchedIssues.isEmpty()) { //resolved, closed상태인 이슈에 해당 태그가 존재하면
            return matchedIssues.stream()
                    .map(Issue::getFixer) //해당 이슈의 fixer를 받아옴
                    .filter(Objects::nonNull)
                    .distinct()
                    .filter(username -> !busyAssignees.contains(username))
                    .limit(3) //최대 3명까지 추천
                    .collect(Collectors.toList());
        } else {
            return userRepository.findByRole(UserRole.DEV).stream()
                    .map(User::getNickname)
                    .filter(username -> !busyAssignees.contains(username))
                    .limit(3)
                    .collect(Collectors.toList());
        }
    }

    public Set<String> getBusyAssignees() {
        List<Issue> busyIssues = issueJpaRepository.findByStatusIn(List.of("assigned"));
        return busyIssues.stream()
                .map(Issue::getAssignee)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

}
