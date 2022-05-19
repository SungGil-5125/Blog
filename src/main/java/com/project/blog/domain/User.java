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
@Table(name = "user")
@Entity
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long user_id;

    @Column
    private String email;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String desc;

    @Column(nullable = true)
    private String refreshToken;

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Board> board = new ArrayList<>();

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
