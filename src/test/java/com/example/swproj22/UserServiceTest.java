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
                .loginId("test")
                .password("password22")
                .nickname("tester")
                .role(UserRole.TESTER)
                .build();

        joinRequest = new JoinRequest();
        joinRequest.setLoginId("test");
        joinRequest.setPassword("password22");
        joinRequest.setPasswordCheck("password22");
        joinRequest.setNickname("tester");
        joinRequest.setRole("TESTER");

        loginRequest = new LoginRequest();
        loginRequest.setLoginId("test");
        loginRequest.setPassword("password22");
    }

    @Test
    void testCheckLoginIdDuplicate() {
        // Mock 설정
        when(userRepository.existsByLoginId("test")).thenReturn(true);

        // 테스트 메서드 호출
        boolean isDuplicate = userService.checkLoginIdDuplicate("test");

        // 결과 검증
        assertTrue(isDuplicate, "중복된 loginId를 예상하였으나, 그렇지 않았습니다");
    }

    @Test
    void testCheckNicknameDuplicate() {
        // Mock 설정
        when(userRepository.existsByNickname("tester")).thenReturn(true);

        // 테스트 메서드 호출
        boolean isDuplicate = userService.checkNicknameDuplicate("tester");

        // 결과 검증
        assertTrue(isDuplicate, "중복된 nickname을 예상하였으나, 그렇지 않았습니다");
    }

    @Test
    void testJoin() {
        // Mock 설정
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userRepository.findByLoginId("test")).thenReturn(Optional.of(user));

        // 테스트 메서드 호출
        userService.join(joinRequest);

        Optional<User> savedUser = userRepository.findByLoginId("test");

        // 결과 검증
        assertTrue(savedUser.isPresent());
        assertEquals("test", savedUser.get().getLoginId());
    }

    @Test
    void testLoginSuccess() {
        // Mock 설정
        when(userRepository.findByLoginId("test")).thenReturn(Optional.of(user));

        // 테스트 메서드 호출
        User loggedInUser = userService.login(loginRequest);

        // 결과 검증
        assertNotNull(loggedInUser, "null이 아님을 예상하였으나, 그렇지 않았습니다");
        assertEquals("test", loggedInUser.getLoginId());
    }

    @Test
    void testLoginFailure() {
        // Mock 설정
        when(userRepository.findByLoginId("test")).thenReturn(Optional.empty());

        // 테스트 메서드 호출
        User loggedInUser = userService.login(loginRequest);

        // 결과 검증
        assertNull(loggedInUser, "null임을 예상하였으나, 그렇지 않았습니다");
    }

    @Test
    void testGetLoginUserByLoginId() {
        // Mock 설정
        when(userRepository.findByLoginId("test")).thenReturn(Optional.of(user));

        // 테스트 메서드 호출
        User foundUser = userService.getLoginUserByLoginId("test");

        // 결과 검증
        assertNotNull(foundUser, "null이 아님을 예상하였으나, 그렇지 않았습니다");
        assertEquals("test", foundUser.getLoginId());
    }
}
