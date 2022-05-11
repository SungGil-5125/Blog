package com.project.blog.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Table(name = "image")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "image_id")
    private Long image_id;

    @ManyToOne(targetEntity = Board.class)
    @JoinColumn(name = "board_id")
    private String board_id;

    @Column(nullable = false)
    private String origFileName;

    @Column(nullable = false)
    private String filePath;

}
