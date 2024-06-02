package com.example.swproj22;

import com.example.swproj22.domain.UserRole;
import com.example.swproj22.domain.dto.JoinRequest;
import com.example.swproj22.domain.dto.LoginRequest;
import com.example.swproj22.domain.entity.User;
import com.example.swproj22.repository.UserRepository;
import com.example.swproj22.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private JoinRequest joinRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder()
                .loginId("testuser")
                .password("password123")
                .nickname("tester")
                .role(UserRole.TESTER)
                .build();

        joinRequest = new JoinRequest();
        joinRequest.setLoginId("testuser");
        joinRequest.setPassword("password123");
        joinRequest.setPasswordCheck("password123");
        joinRequest.setNickname("tester");
        joinRequest.setRole("TESTER");

        loginRequest = new LoginRequest();
        loginRequest.setLoginId("testuser");
        loginRequest.setPassword("password123");
    }

    @Test
    void testCheckLoginIdDuplicate() {
        // Mock 설정
        when(userRepository.existsByLoginId("testuser")).thenReturn(true);

        // 테스트 메서드 호출
        boolean isDuplicate = userService.checkLoginIdDuplicate("testuser");

        // 결과 검증
        assertTrue(isDuplicate, "Expected login ID to be duplicate, but it was not");
    }

    @Test
    void testCheckNicknameDuplicate() {
        // Mock 설정
        when(userRepository.existsByNickname("tester")).thenReturn(true);

        // 테스트 메서드 호출
        boolean isDuplicate = userService.checkNicknameDuplicate("tester");

        // 결과 검증
        assertTrue(isDuplicate, "Expected nickname to be duplicate, but it was not");
    }

    @Test
    void testJoin() {
        // Mock 설정
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.of(user));

        // 테스트 메서드 호출
        userService.join(joinRequest);

        // 추가로, 저장된 사용자 정보를 검증하는 로직을 추가할 수 있음
        Optional<User> savedUser = userRepository.findByLoginId("testuser");

        // 결과 검증
        assertTrue(savedUser.isPresent());
        assertEquals("testuser", savedUser.get().getLoginId());
    }

    @Test
    void testLoginSuccess() {
        // Mock 설정
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.of(user));

        // 테스트 메서드 호출
        User loggedInUser = userService.login(loginRequest);

        // 결과 검증
        assertNotNull(loggedInUser, "Expected user to be not null, but it was null");
        assertEquals("testuser", loggedInUser.getLoginId());
    }

    @Test
    void testLoginFailure() {
        // Mock 설정
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.empty());

        // 테스트 메서드 호출
        User loggedInUser = userService.login(loginRequest);

        // 결과 검증
        assertNull(loggedInUser, "Expected user to be null, but it was not");
    }

    @Test
    void testGetLoginUserByLoginId() {
        // Mock 설정
        when(userRepository.findByLoginId("testuser")).thenReturn(Optional.of(user));

        // 테스트 메서드 호출
        User foundUser = userService.getLoginUserByLoginId("testuser");

        // 결과 검증
        assertNotNull(foundUser, "Expected user to be not null, but it was null");
        assertEquals("testuser", foundUser.getLoginId());
    }
}
