package com.project.blog.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor // 기본 생성자를 해주는거거든
@AllArgsConstructor
@Entity
public class User implements UserDetails {

    // 애플리케이션이 가지고 있는 유저 정보와 sping Security가 사용하는 Authentication(인증) 객체 사이의 어댑터이다.

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long user_id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String password;

    @Column(nullable = true)
    private String refreshToken;

    @Column(nullable = true)
    private String url;

    @OneToMany(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "board_id")
    private List<Board> boards = new ArrayList<>();

    public void update(String name, String password){
        this.name = name;
        this.password = password;
    }

    public void updateUrl(String url) {
        this.url = url;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
