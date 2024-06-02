package com.example.swproj22;

import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.Tag;
import com.example.swproj22.domain.UserRole;
import com.example.swproj22.domain.entity.User;
import com.example.swproj22.dto.IssueCountRequest;
import com.example.swproj22.repository.IssueJpaRepository;
import com.example.swproj22.repository.TagRepository;
import com.example.swproj22.repository.UserRepository;
import com.example.swproj22.service.AnalyzeService;
import com.example.swproj22.service.RecommendService;
import com.example.swproj22.service.TagService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class TestCase2 {

    @Mock
    private IssueJpaRepository issueJpaRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AnalyzeService analyzeService;
    @InjectMocks
    private TagService tagService;
    @InjectMocks
    private RecommendService recommendService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setCategory("bug");

        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setCategory("feature");

        List<Tag> tags = Arrays.asList(tag1, tag2);
        when(tagRepository.findAll()).thenReturn(tags);
    }

    @Test
    public void testCountIssuesByDay() {
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"2024-05-01", 5L},
                new Object[]{"2024-05-02", 3L},
                new Object[]{"2024-05-03", 8L}
        );

        when(issueJpaRepository.countIssuesByDayUsingCast()).thenReturn(mockResults);

        Map<Integer, Long> result = analyzeService.countIssuesByDay();

        assertEquals(3, result.size());
        assertEquals(5L, result.get(1));
        assertEquals(3L, result.get(2));
        assertEquals(8L, result.get(3));
    }

    @Test
    public void testCountIssuesByMonth() {
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"2024", 6, 10L},
                new Object[]{"2024", 7, 15L}
        );

        when(issueJpaRepository.countIssuesByMonth()).thenReturn(mockResults);

        Map<String, Long> result = analyzeService.countIssuesByMonth();

        assertEquals(2, result.size());
        assertEquals(10L, result.get("2024-06"));
        assertEquals(15L, result.get("2024-07"));
    }

    @Test
    public void testCountIssuesByStatus() {
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"new", 7L},
                new Object[]{"assigned", 5L}
        );

        when(issueJpaRepository.countIssuesByStatus()).thenReturn(mockResults);

        Map<String, Long> result = analyzeService.countIssuesByStatus();

        assertEquals(2, result.size());
        assertEquals(7L, result.get("new"));
        assertEquals(5L, result.get("assigned"));
    }

    @Test
    public void testCountIssuesByTag() {
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"bug", 12L},
                new Object[]{"feature", 8L}
        );

        when(issueJpaRepository.countIssuesByTag()).thenReturn(mockResults);

        Map<String, Long> result = analyzeService.countIssuesByTag();

        assertEquals(2, result.size());
        assertEquals(12L, result.get("bug"));
        assertEquals(8L, result.get("feature"));
    }

    @Test
    public void testGetAllTags() {
        List<Tag> tags = tagService.getAllTags();

        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertEquals("bug", tags.get(0).getCategory());
        assertEquals("feature", tags.get(1).getCategory());
    }


    @Test
    public void testRecommendAssigneesByTag_withResolvedIssues_andBusyAssignees() {
        Tag tag = new Tag();
        tag.setCategory("bug");

        Issue issue1 = new Issue();
        issue1.setFixer("user1");

        Issue issue2 = new Issue();
        issue2.setFixer("user2");

        List<Issue> issues = Arrays.asList(issue1, issue2);

        // "assigned" 상태의 이슈를 담당하고 있는 assignee
        Issue busyIssue = new Issue();
        busyIssue.setAssignee("user2");

        List<Issue> busyIssues = Collections.singletonList(busyIssue);

        when(issueJpaRepository.findByStatusesAndTags(Arrays.asList("resolved", "closed"), Collections.singletonList(tag)))
                .thenReturn(issues);
        when(issueJpaRepository.findByStatusIn(Collections.singletonList("assigned")))
                .thenReturn(busyIssues);
        when(userRepository.findByNickname("user1")).thenReturn(new User());
        when(userRepository.findByNickname("user2")).thenReturn(new User());

        List<String> result = recommendService.recommendAssigneesByTag(Collections.singletonList(tag));

        assertEquals(1, result.size());
        assertEquals("user1", result.get(0));  // busyAssignees에 포함되지 않은 user1만 추천됨
    }


    @Test
    public void testRecommendAssigneesByTag_noResolvedIssues_andBusyAssignees() {
        Tag tag = new Tag();
        tag.setCategory("bug");

        User user1 = new User();
        user1.setNickname("user1");
        user1.setRole(UserRole.DEV);

        User user2 = new User();
        user2.setNickname("user2");
        user2.setRole(UserRole.DEV);

        when(issueJpaRepository.findByStatusesAndTags(Arrays.asList("resolved", "closed"), Collections.singletonList(tag)))
                .thenReturn(Collections.emptyList());
        when(userRepository.findByRole(UserRole.DEV)).thenReturn(Arrays.asList(user1, user2));

        // "assigned" 상태의 이슈를 담당하고 있는 assignee
        Issue busyIssue = new Issue();
        busyIssue.setAssignee("user2");
        List<Issue> busyIssues = Collections.singletonList(busyIssue);
        when(issueJpaRepository.findByStatusIn(Collections.singletonList("assigned")))
                .thenReturn(busyIssues);

        List<String> result = recommendService.recommendAssigneesByTag(Collections.singletonList(tag));

        assertEquals(1, result.size()); // busyAssignee가 제외되었는지 확인
        assertEquals("user1", result.get(0)); // busyAssignee가 아닌 user1이 추천되었는지 확인
    }


    @Test
    public void testGetBusyAssignees_withMixedStatuses() {
        Issue assignedIssue = new Issue();
        assignedIssue.setAssignee("user1");
        assignedIssue.setStatus("assigned");

        Issue closedIssue = new Issue();
        closedIssue.setAssignee("user2");
        closedIssue.setStatus("closed");

        Issue resolvedIssue = new Issue();
        resolvedIssue.setAssignee("user3");
        resolvedIssue.setStatus("resolved");

        List<Issue> issues = Arrays.asList(assignedIssue, closedIssue, resolvedIssue);
        when(issueJpaRepository.findByStatusIn(Collections.singletonList("assigned")))
                .thenReturn(issues.stream().filter(issue -> "assigned".equals(issue.getStatus())).collect(Collectors.toList()));

        Set<String> result = recommendService.getBusyAssignees();

        assertEquals(1, result.size());
        assertTrue(result.contains("user1"));  // "assigned" 상태의 담당자만 포함되어야 함
        assertFalse(result.contains("user2")); // "closed" 상태의 담당자는 포함되지 않아야 함
        assertFalse(result.contains("user3")); // "resolved" 상태의 담당자는 포함되지 않아야 함
    }

}
