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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

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

        // Mock 데이터 설정
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
                new Object[]{"2024-05-02", 3L}
        );

        when(issueJpaRepository.countIssuesByDayUsingCast()).thenReturn(mockResults);

        List<IssueCountRequest> result = analyzeService.countIssuesByDay();

        assertEquals(2, result.size());
        assertEquals("2024-05-01", result.get(0).getDate());
        assertEquals(5L, result.get(0).getIssues());
        assertEquals("2024-05-02", result.get(1).getDate());
        assertEquals(3L, result.get(1).getIssues());
    }

    @Test
    public void testCountIssuesByMonth() {
        List<Object[]> mockResults = Arrays.asList(
                new Object[]{"2024", 5, 10L},
                new Object[]{"2024", 6, 15L}
        );

        when(issueJpaRepository.countIssuesByMonth()).thenReturn(mockResults);

        List<IssueCountRequest> result = analyzeService.countIssuesByMonth();

        assertEquals(2, result.size());
        assertEquals("2024-05", result.get(0).getDate());
        assertEquals(10L, result.get(0).getIssues());
        assertEquals("2024-06", result.get(1).getDate());
        assertEquals(15L, result.get(1).getIssues());
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
    /*
    AnalyzeService 클래스의 메소드를 테스트하여 각 메소드가 의도한 대로 동작하는지 확인하고,
    주어진 데이터를 기반으로 올바른 결과를 반환하는지 검증합니다.
    이 테스트는 서비스 계층의 비즈니스 로직이 예상대로 작동하는지 확인하기 위한 것입니다.
    */


    @Test
    public void testGetAllTags() {
        /*
        TagService 클래스의 메소드들이 의도한 대로 동작하는지 확인하기 위해 테스트 코드를 작성합니다.
        특히, getAllTags 메소드가 tagRepository로부터 모든 태그 목록을 올바르게 반환하는지 확인합니다.
        이 테스트는 TagRepository가 실제로 동작하지 않고, Mock 객체를 사용하여 테스트합니다.
        이를 통해 데이터베이스에 대한 의존성을 제거하고, 단위 테스트를 보다 빠르고 안전하게 수행할 수 있습니다.
         */
        List<Tag> tags = tagService.getAllTags();

        assertNotNull(tags);
        assertEquals(2, tags.size());
        assertEquals("bug", tags.get(0).getCategory());
        assertEquals("feature", tags.get(1).getCategory());
    }


    @Test
    public void testRecommendAssigneesByTag_WithMatchedIssues() {
        Tag tag1 = new Tag();
        tag1.setId(1L);
        tag1.setCategory("bug");

        Issue issue1 = new Issue();
        issue1.setAssignee("dev1");
        issue1.setTags(Collections.singletonList(tag1));
        issue1.setStatus("resolved");

        User user1 = new User();
        user1.setNickname("dev1");
        user1.setRole(UserRole.DEV);

        when(issueJpaRepository.findByStatusesAndTags(anyList(), anyList())).thenReturn(Collections.singletonList(issue1));
        when(userRepository.findByNickname("dev1")).thenReturn(user1);

        List<String> recommendedAssignees = recommendService.recommendAssigneesByTag(Collections.singletonList(tag1));

        assertEquals(1, recommendedAssignees.size());
        assertEquals("dev1", recommendedAssignees.get(0));
    }

    @Test
    public void testRecommendAssigneesByTag_WithoutMatchedIssues() {
        User user1 = new User();
        user1.setNickname("dev1");
        user1.setRole(UserRole.DEV);

        User user2 = new User();
        user2.setNickname("dev2");
        user2.setRole(UserRole.DEV);

        when(issueJpaRepository.findByStatusesAndTags(anyList(), anyList())).thenReturn(Collections.emptyList());
        when(userRepository.findByRole(UserRole.DEV)).thenReturn(Arrays.asList(user1, user2));

        List<String> recommendedAssignees = recommendService.recommendAssigneesByTag(Collections.emptyList());

        assertEquals(2, recommendedAssignees.size());
        assertTrue(recommendedAssignees.contains("dev1"));
        assertTrue(recommendedAssignees.contains("dev2"));
    }

    @Test
    public void testGetBusyAssignees() {
        Issue issue1 = new Issue();
        issue1.setFixer("dev1");
        issue1.setStatus("assigned");

        Issue issue2 = new Issue();
        issue2.setFixer("dev2");
        issue2.setStatus("resolved");

        when(issueJpaRepository.findByStatusIn(anyList())).thenReturn(Arrays.asList(issue1, issue2));

        Set<String> busyAssignees = recommendService.getBusyAssignees();

        assertEquals(2, busyAssignees.size());
        assertTrue(busyAssignees.contains("dev1"));
        assertTrue(busyAssignees.contains("dev2"));
    }
    /*
    RecommendService 클래스의 메소드들이 의도한 대로 동작하는지 확인하기 위해 테스트 코드를 작성합니다.
    특히, recommendAssigneesByTag 메소드와 getBusyAssignees 메소드가 올바르게 작동하는지 검증합니다.
    이 테스트는 IssueJpaRepository와 UserRepository가 실제로 동작하지 않고, Mock 객체를 사용하여 테스트합니다.
    이를 통해 데이터베이스에 대한 의존성을 제거하고, 단위 테스트를 보다 빠르고 안전하게 수행할 수 있습니다.
    태그가 포함된 이슈가 있을 때 recommendAssigneesByTag 메소드가 올바르게 작동하는지 테스트합니다.
    태그가 포함된 이슈가 없을 때 recommendAssigneesByTag 메소드가 올바르게 작동하는지 테스트합니다.
    getBusyAssignees 메소드가 올바르게 작동하여 assigned 또는 resolved 상태인 이슈의 fixer를 반환하는지 테스트합니다.
     */
}
