package com.project.blog.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Table(name = "img")
public class Img {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "img_id")
    private Long id;

    @ManyToMany
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false)
    private String origFileName;

    @Column(nullable = false)
    private String filePath;

}
