package com.example.swproj22;

import com.example.swproj22.bootstrap.TagLoader;
import com.example.swproj22.domain.Issue;
import com.example.swproj22.domain.Tag;
import com.example.swproj22.domain.UserRole;
import com.example.swproj22.domain.entity.User;
import com.example.swproj22.dto.*;
import com.example.swproj22.repository.TagRepository;
import com.example.swproj22.repository.IssueJpaRepository;
import com.example.swproj22.repository.UserRepository;
import com.example.swproj22.service.AnalyzeService;
import com.example.swproj22.service.IssueService;
import com.example.swproj22.service.TagService;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Swproj22ApplicationTests {

	@Test
	void contextLoads() {
	}


	@Mock
	private TagRepository tagRepository;
	@Mock
	private IssueJpaRepository issueJpaRepository;
	@Mock
	private UserRepository userRepository;
	@InjectMocks
	private TagService tagService;
	@InjectMocks
	private IssueService issueService;

	@InjectMocks
	private AnalyzeService analyzeService;

	/*Issue Test Code*/
	private IssueCreateRequest issueCreateRequest;
	private Tag tag1;
	private Tag tag2;

	@BeforeEach
	void setUp() {
		tag1 = new Tag(1L, "tag1");
		tag2 = new Tag(2L, "tag2");
		MockitoAnnotations.openMocks(this);

		List<Long> tagIds = Arrays.asList(1L, 2L);
		issueCreateRequest = IssueCreateRequest.builder()
				.projectId(1L)
				.title("title")
				.description("description")
				.code("code")
				.reporter("reporter")
				.tags(tagIds)
				.priority("high")
				.build();

		when(tagRepository.findById(1L)).thenReturn(Optional.of(tag1));
		when(tagRepository.findById(2L)).thenReturn(Optional.of(tag2));
	}
	/*이슈 생성 */
	@Test
	void createIssue_success() {
		Issue expectedIssue = Issue.builder()
				.projectId(issueCreateRequest.getProjectId())
				.title(issueCreateRequest.getTitle())
				.description(issueCreateRequest.getDescription())
				.code(issueCreateRequest.getCode())
				.status("new")
				.reporter(issueCreateRequest.getReporter())
				.tags(Arrays.asList(tag1, tag2))
				.priority(issueCreateRequest.getPriority())
				.reportedTime(LocalDateTime.now())
				.build();

		when(issueJpaRepository.save(any(Issue.class))).thenReturn(expectedIssue);

		Issue createdIssue = issueService.createIssue(issueCreateRequest);

		assertNotNull(createdIssue);
		assertEquals(issueCreateRequest.getProjectId(), createdIssue.getProjectId());
		assertEquals(issueCreateRequest.getTitle(), createdIssue.getTitle());
		assertEquals(issueCreateRequest.getDescription(), createdIssue.getDescription());
		assertEquals(issueCreateRequest.getCode(), createdIssue.getCode());
		assertEquals("new", createdIssue.getStatus());
		assertEquals(issueCreateRequest.getReporter(), createdIssue.getReporter());
		assertEquals(2, createdIssue.getTags().size());
		assertEquals(issueCreateRequest.getPriority(), createdIssue.getPriority());

		verify(tagRepository, times(2)).findById(anyLong());
		verify(issueJpaRepository, times(1)).save(any(Issue.class));
	}

	@Test
	void createIssue_tagNotFound() {
		when(tagRepository.findById(1L)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			issueService.createIssue(issueCreateRequest);
		});

		assertEquals("Tag not found with id: 1", exception.getMessage());
		verify(tagRepository, times(1)).findById(1L);
		verify(tagRepository, times(0)).findById(2L);
		verify(issueJpaRepository, times(0)).save(any(Issue.class));
	}

	/*이슈 아이디로 이슈가져오기*/
	@Test
	void getIssueByIssueId_Success() {
		// Given
		Long issueId = 1L;
		Issue issue = Issue.builder().id(issueId).build();
		when(issueJpaRepository.findById(issueId)).thenReturn(Optional.of(issue));

		// When
		Issue result = issueService.getIssueByIssueId(issueId);

		// Then
		assertNotNull(result);
		assertEquals(issueId, result.getId());

		verify(issueJpaRepository, times(1)).findById(issueId);
	}

	/*코드 수정 */
	@Test
	void editIssueCode_Success() {
		// Given
		Long issueId = 1L;
		String newCode = "newCode";
		IssueEditCodeRequest editCodeRequest = new IssueEditCodeRequest(newCode);
		Issue issue = new Issue();
		issue.setId(issueId);
		issue.setCode("oldCode");
		when(issueJpaRepository.findById(issueId)).thenReturn(Optional.of(issue));
		when(issueJpaRepository.save(issue)).thenReturn(issue);

		// When
		Issue result = issueService.editIssueCode(issueId, editCodeRequest);

		// Then
		assertNotNull(result);
		assertEquals(newCode, result.getCode());

		verify(issueJpaRepository, times(1)).findById(issueId);
		verify(issueJpaRepository, times(1)).save(issue);
	}

	/*상태 변경 (대표적으로, fixed로 변경) */
	@Test
	void changeIssueStatus_fixed_Success() {
		// Given
		Long issueId = 1L;
		IssueStatusChangeRequest statusChangeRequest = IssueStatusChangeRequest.builder()
				.status("fixed")
				.nickname("DEV") // Assuming DEV user is changing the status to fixed
				.build();

		Issue issue = Issue.builder()
				.id(issueId)
				.status("new")
				.build();

		User devUser = User.builder()
				.nickname("DEV")
				.role(UserRole.DEV)
				.build();

		// Mocking the saved issue
		Issue savedIssue = Issue.builder()
				.id(issueId)
				.status(statusChangeRequest.getStatus()) // Setting the status to the new status
				.build();

		when(issueJpaRepository.findById(issueId)).thenReturn(Optional.of(issue));
		when(userRepository.findByNickname("DEV")).thenReturn(devUser);
		when(issueJpaRepository.save(any(Issue.class))).thenReturn(savedIssue); // Mocking the save operation

		// When
		Issue updatedIssue = issueService.changeIssueStatus(issueId, statusChangeRequest);

		// Then
		assertNotNull(updatedIssue);
		assertEquals("fixed", updatedIssue.getStatus());

		verify(issueJpaRepository, times(1)).findById(issueId);
		verify(userRepository, times(1)).findByNickname("DEV");
		verify(issueJpaRepository, times(1)).save(any(Issue.class)); // Verifying that save method is called with any Issue object
	}

	/*상태 assigned로 변경 */
	@Test
	void changeIssueAssigned_Success() {
		// Given
		Long issueId = 1L;
		Issue issue = new Issue();
		issue.setId(issueId);
		issue.setStatus("new");
		when(issueJpaRepository.findById(issueId)).thenReturn(Optional.of(issue));
		when(issueJpaRepository.save(issue)).thenReturn(issue); // 이 부분 추가

		// When
		Issue updatedIssue = issueService.changeIssueAssigned(issueId);

		// Then
		assertNotNull(updatedIssue);
		assertEquals("assigned", updatedIssue.getStatus());

		verify(issueJpaRepository, times(1)).findById(issueId);
		verify(issueJpaRepository, times(1)).save(issue);
	}

	/*Assignee 배정 */
	@Test
	void changeIssueAssignee_Success() {
		// Given
		Long issueId = 1L;
		IssueMangerChangeRequest issueMangerChangeRequest = IssueMangerChangeRequest.builder()
				.nickname("newAssignee")
				.pl("PL") // Assuming PL user is changing the assignee
				.build();

		Issue issue = Issue.builder()
				.id(issueId)
				.status("new")
				.build();

		User plUser = User.builder()
				.nickname("PL")
				.role(UserRole.PL)
				.build();

		Issue updatedIssue = Issue.builder()
				.id(issueId)
				.status("new")
				.assignee("newAssignee")
				.build();

		when(issueJpaRepository.findById(issueId)).thenReturn(Optional.of(issue));
		when(userRepository.findByNickname("PL")).thenReturn(plUser);
		when(issueJpaRepository.save(any(Issue.class))).thenReturn(updatedIssue); // Mocking the save operation

		// When
		Issue result = issueService.changeIssueAssignee(issueId, issueMangerChangeRequest);

		// Then
		assertNotNull(result);
		assertEquals("newAssignee", result.getAssignee());

		verify(issueJpaRepository, times(1)).findById(issueId);
		verify(userRepository, times(1)).findByNickname("PL");
		verify(issueJpaRepository, times(1)).save(any(Issue.class)); // Verifying that save method is called with any Issue object
	}

	@Test
	void changeIssueAssignee_InvalidUserRole() {
		// Given
		Long issueId = 1L;
		IssueMangerChangeRequest issueMangerChangeRequest = IssueMangerChangeRequest.builder()
				.pl("PL")
				.nickname("newAssignee") // Assuming Tester user is trying to change the assignee
				.build();

		Issue issue = Issue.builder()
				.id(issueId)
				.status("new")
				.build();

		User testerUser = User.builder()
				.nickname("Tester")
				.role(UserRole.TESTER) // Tester user does not have PL role
				.build();

		when(issueJpaRepository.findById(issueId)).thenReturn(Optional.of(issue));
		when(userRepository.findByNickname("PL")).thenReturn(testerUser);

		// When & Then
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			issueService.changeIssueAssignee(issueId, issueMangerChangeRequest);
		});

		assertEquals("Only users with 'PL' role can change assignee.", exception.getMessage());

		verify(issueJpaRepository, times(1)).findById(issueId);
		verify(userRepository, times(1)).findByNickname("PL");
		verify(issueJpaRepository, never()).save(any(Issue.class)); // Verifying that save method is not called
	}


	/*Fixer 배정 */
	@Test
	void changeIssueFixer_Success() {
		// Given
		Long issueId = 1L;
		Issue issue = new Issue();
		issue.setId(issueId);
		issue.setAssignee("assignee");
		when(issueJpaRepository.findById(issueId)).thenReturn(Optional.of(issue));
		when(issueJpaRepository.save(issue)).thenReturn(issue); // 이 부분 추가

		// When
		Issue updatedIssue = issueService.changeIssueFixer(issueId);

		// Then
		assertNotNull(updatedIssue);
		assertEquals("assignee", updatedIssue.getFixer());

		verify(issueJpaRepository, times(1)).findById(issueId);
		verify(issueJpaRepository, times(1)).save(issue);
	}

	/*Browse 기능 */
	@Test
	void browse_Success() {
		// Given
		Long projectId = 1L;
		Issue issue1 = Issue.builder().projectId(projectId).build();
		Issue issue2 = Issue.builder().projectId(projectId).build();
		when(issueJpaRepository.findByProjectId(projectId)).thenReturn(Arrays.asList(issue1, issue2));

		// When
		List<Issue> result = issueService.browse(projectId);

		// Then
		assertNotNull(result);
		assertEquals(2, result.size());

		verify(issueJpaRepository, times(1)).findByProjectId(projectId);
	}

	/*상태로 이슈찾기 */
	@Test
	void searchByState_Success() {
		// Given
		Long projectId = 1L;
		String state = "new";
		Issue issue1 = Issue.builder().projectId(projectId).status(state).build();
		Issue issue2 = Issue.builder().projectId(projectId).status(state).build();
		when(issueJpaRepository.findByProjectIdAndStatus(projectId, state)).thenReturn(Arrays.asList(issue1, issue2));

		// When
		List<Issue> result = issueService.searchByState(projectId, state);

		// Then
		assertNotNull(result);
		assertEquals(2, result.size());
		result.forEach(issue -> assertEquals(state, issue.getStatus()));

		verify(issueJpaRepository, times(1)).findByProjectIdAndStatus(projectId, state);
	}

	/*리포터한 이슈 검색 */
	@Test
	void getIssuesByProjectAndReporter_Success() {
		// Given
		Long projectId = 1L;
		String reporter = "John";
		Issue issue1 = Issue.builder().projectId(projectId).reporter(reporter).build();
		Issue issue2 = Issue.builder().projectId(projectId).reporter(reporter).build();
		when(issueJpaRepository.findByProjectIdAndReporter(projectId, reporter)).thenReturn(Arrays.asList(issue1, issue2));

		// When
		List<Issue> result = issueService.getIssuesByProjectAndReporter(projectId, reporter);

		// Then
		assertNotNull(result);
		assertEquals(2, result.size());
		result.forEach(issue -> assertEquals(reporter, issue.getReporter()));

		verify(issueJpaRepository, times(1)).findByProjectIdAndReporter(projectId, reporter);
	}

	/*배정된 이슈 검색 */
	@Test
	void getIssuesByProjectAndAssignee_Success() {
		// Given
		Long projectId = 1L;
		String assignee = "Alice";
		Issue issue1 = Issue.builder().projectId(projectId).assignee(assignee).build();
		Issue issue2 = Issue.builder().projectId(projectId).assignee(assignee).build();
		when(issueJpaRepository.findByProjectIdAndAssignee(projectId, assignee)).thenReturn(Arrays.asList(issue1, issue2));

		// When
		List<Issue> result = issueService.getIssuesByProjectAndAssignee(projectId, assignee);

		// Then
		assertNotNull(result);
		assertEquals(2, result.size());
		result.forEach(issue -> assertEquals(assignee, issue.getAssignee()));

		verify(issueJpaRepository, times(1)).findByProjectIdAndAssignee(projectId, assignee);
	}

}