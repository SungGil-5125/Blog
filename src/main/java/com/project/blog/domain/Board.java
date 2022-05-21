package com.project.blog.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "board")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long board_id;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = User.class) // merge란 엔티티 상태를 병합할 때 연관된 엔티티도 모두 병합하는것을 의미함.
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String date;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    private List<Image> image = new ArrayList<>();

    public void mapping(User user) {
        this.user = user;
        user.getBoard().add(this);
    }
}
