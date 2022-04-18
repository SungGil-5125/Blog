package com.project.blog.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@Table(name = "user")
@NoArgsConstructor // 기본 생성자를 해주는거거든
@AllArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

}
