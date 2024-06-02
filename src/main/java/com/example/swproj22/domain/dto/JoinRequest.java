package com.example.swproj22.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.example.swproj22.domain.UserRole;
import com.example.swproj22.domain.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

    @NotBlank(message = "로그인 아이디를 입력해야 합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력해야 합니다.")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "닉네임을 입력해야 합니다,")
    private String nickname;

    private String role;

    public User toEntity() {
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .role(UserRole.valueOf(this.role.toUpperCase()))
                .build();
    }
}