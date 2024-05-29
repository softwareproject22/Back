package com.example.swproj22.controller;

import com.example.swproj22.domain.UserRole;
import com.example.swproj22.domain.dto.JoinRequest;
import com.example.swproj22.domain.dto.LoginRequest;
import com.example.swproj22.domain.entity.User;
import com.example.swproj22.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
public class LoginController {

    private final UserService userService;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<?> home(@SessionAttribute(name = "userId", required = false) Long userId) {
        User loginUser = userService.getLoginUserById(userId);

        Map<String, Object> response = new HashMap<>();
        if (loginUser != null) {
            response.put("nickname", loginUser.getNickname());
            response.put("message", "User is logged in");
        } else {
            response.put("message", "User is not logged in");
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/join")
    public ResponseEntity<Map<String, String>> join(@Valid @RequestBody JoinRequest joinRequest, BindingResult bindingResult) {
        Map<String, String> response = new HashMap<>();

        // loginId 중복 체크
        if(userService.checkLoginIdDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "로그인 아이디가 중복됩니다."));
        }
        // 닉네임 중복 체크
        if(userService.checkNicknameDuplicate(joinRequest.getNickname())) {
            bindingResult.addError(new FieldError("joinRequest", "nickname", "닉네임이 중복됩니다."));
        }
        // password와 passwordCheck가 같은지 체크
     if(!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }
        // 유효한 role인지 확인
        try {
            UserRole.valueOf(joinRequest.getRole().name());
        } catch (IllegalArgumentException | NullPointerException e) {
            bindingResult.addError(new FieldError("joinRequest", "role", "존재하지 않는 role입니다"));
        }

        if(bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                response.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        userService.join(joinRequest);
        response.put("message", "회원 가입 성공");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult,
                                   HttpServletRequest httpServletRequest) {
        User user = userService.login(loginRequest);
        Map<String, String> loginresponse = new HashMap<>();

        if (user == null) {
            // 로그인 아이디나 비밀번호가 틀린 경우 필드 오류 추가
            bindingResult.addError(new FieldError("loginRequest", "loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다."));
        }
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                loginresponse.put(error.getField(), error.getDefaultMessage());
            });
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(loginresponse);
        }

        // 로그인 성공하면 세션이 생성됨
        httpServletRequest.getSession().invalidate(); // 세션을 생성하기 전에 기존 세션 파기
        HttpSession session = httpServletRequest.getSession(true); // 세션 없으면 생성
        session.setAttribute("userId", user.getId()); // 세션에 userId를 넣어줌
        session.setMaxInactiveInterval(3600); //세션 1시간 동안 유지

        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false); // 세션 없으면 null return
        if (session != null) {
            session.invalidate();
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logout successful");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/login/info")
    public ResponseEntity<?> userInfo(@SessionAttribute(name = "userId", required = false) Long userId) {
        User loginUser = userService.getLoginUserById(userId);

        if (loginUser == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "User not logged in");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("loginId", loginUser.getLoginId());
        response.put("role", loginUser.getRole());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/login/userlist") //admin일때만 적용
    public ResponseEntity<?> getUserList() {
        List<User> users = userService.getAllUsers();

        List<Map<String, Object>> userList = users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("loginId", user.getLoginId());
            userMap.put("role", user.getRole());
            return userMap;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(userList);
    }
}