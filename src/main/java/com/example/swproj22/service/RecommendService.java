package com.example.swproj22.service;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.Tag;
import com.example.swproj22.domain.UserRole;
import com.example.swproj22.domain.entity.User;
import com.example.swproj22.repository.IssueJpaRepository;
import com.example.swproj22.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.Objects;

public class RecommendService {

    private final IssueJpaRepository issueJpaRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecommendService(IssueJpaRepository issueJpaRepository, UserRepository userRepository){
        this.issueJpaRepository = issueJpaRepository;
        this.userRepository = userRepository;
    }

    public List<String> recommendAssigneesByTag(List<Tag> tags) {
        List<Issue> matchedIssues = issueJpaRepository.findByStatusAndTags("fixed", tags);
        Set<String> busyAssignees = getBusyAssignees();

        if (!matchedIssues.isEmpty()) {
            return matchedIssues.stream()
                    .map(Issue::getAssignee)
                    .map(userRepository::findByNickname) // User 객체를 직접 반환한다고 가정
                    .filter(Objects::nonNull) // null이 아닌 User 객체만 처리
                    .filter(user -> user.getRole() == UserRole.DEV) // Role이 DEV인 사용자만 필터링
                    .map(User::getNickname)
                    .distinct()
                    .filter(username -> !busyAssignees.contains(username))
                    .collect(Collectors.toList());
        } else {
            return userRepository.findByRole(UserRole.DEV).stream()
                    .map(User::getNickname)
                    .filter(username -> !busyAssignees.contains(username))
                    .collect(Collectors.toList());
        }
    }

    private Set<String> getBusyAssignees() {
        List<Issue> busyIssues = issueJpaRepository.findByStatus(List.of("assigned", "resolved"));
        return busyIssues.stream()
                .map(Issue::getAssignee) // 이 부분에서 String username이 반환됩니다.
                .map(userRepository::findByNickname) // username으로 User 객체를 찾습니다.
                .filter(Objects::nonNull) // null이 아닌 User 객체만 처리
                .map(User::getNickname) // User 객체에서 nickname을 추출
                .collect(Collectors.toSet());
    }
}
