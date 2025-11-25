package com.plana.seniorjob.global.validation;

import com.plana.seniorjob.user.dto.AgencySignupRequest;
import org.springframework.stereotype.Component;

@Component
public class AgencySignupValidator {

    private static final String USERNAME_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{5,12}$";
    private static final String PASSWORD_REGEX =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=]).{8,16}$";

    public void validate(AgencySignupRequest req) {

        // 아이디 규칙 체크
        if (!req.getUsername().matches(USERNAME_REGEX)) {
            throw new IllegalArgumentException("영문, 숫자를 포함하여 5~12자를 입력하세요.");
        }

        // 비밀번호 규칙 체크
        if (!req.getPassword().matches(PASSWORD_REGEX)) {
            throw new IllegalArgumentException("영문, 숫자, 특수문자를 포함하여 8~16자를 입력하세요.");
        }

        // 비밀번호 확인 체크
        if (!req.getPassword().equals(req.getPasswordCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }
}
