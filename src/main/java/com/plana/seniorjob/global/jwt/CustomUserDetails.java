package com.plana.seniorjob.global.jwt;

import com.plana.seniorjob.user.entity.UserAgency;
import com.plana.seniorjob.user.entity.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UserEntity kakaoUser;
    private final UserAgency agencyUser;

    public CustomUserDetails(UserEntity kakaoUser) {
        this.kakaoUser = kakaoUser;
        this.agencyUser = null;
    }

    public CustomUserDetails(UserAgency agencyUser) {
        this.kakaoUser = null;
        this.agencyUser = agencyUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (agencyUser != null) {
            return List.of(new SimpleGrantedAuthority("ROLE_AGENCY"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        if (agencyUser != null) return agencyUser.getPassword();
        return ""; // 카카오 로그인은 password 없음
    }

    @Override
    public String getUsername() {
        if (agencyUser != null) return agencyUser.getUsername();
        return kakaoUser.getKakaoId(); 
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
