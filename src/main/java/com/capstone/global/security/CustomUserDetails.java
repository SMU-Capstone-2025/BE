package com.capstone.global.security;


import com.capstone.domain.project.entity.Project;
import com.capstone.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;
    private final List<Project> projects; // ✅ 프로젝트 목록을 생성자로 받음

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        if (projects.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_VIEWER"));
            return authorities;
        }

        for (Project project : projects) {
            Map<String, String> projectAuthorities = project.getAuthorities();
            if (projectAuthorities != null && projectAuthorities.containsKey(user.getEmail())) {
                authorities.add(new SimpleGrantedAuthority(projectAuthorities.get(user.getEmail())));
            }
        }

        if (authorities.isEmpty()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_VIEWER"));
        }

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}