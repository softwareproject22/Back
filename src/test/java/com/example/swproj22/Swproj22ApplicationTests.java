package com.example.swproj22;

import com.example.swproj22.bootstrap.TagLoader;
import com.example.swproj22.domain.Tag;
import com.example.swproj22.dto.TagCreateRequest;
import com.example.swproj22.dto.TagAddToIssueRequest;
import com.example.swproj22.repository.TagRepository;
import com.example.swproj22.repository.IssueJpaRepository;
import com.example.swproj22.service.TagService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class Swproj22ApplicationTests {

	@Test
	void contextLoads() {
	}

	@Mock
	private TagRepository tagRepository;

	@InjectMocks
	private TagService tagService;

	@Test
	public void testCreateTag() {
		// Setup
		TagCreateRequest request = new TagCreateRequest();
		request.setCategory("Bug");

		Tag expectedTag = new Tag();
		expectedTag.setCategory("Bug");

		when(tagRepository.save(any(Tag.class))).thenReturn(expectedTag);

		// Execute
		Tag result = tagService.createTag(request);

		// Verify
		assertEquals("Bug", result.getCategory());
		verify(tagRepository).save(any(Tag.class)); // 확인: save 메서드가 호출되었는지
	}
}
