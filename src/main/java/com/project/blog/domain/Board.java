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

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class) // merge란 엔티티 상태를 병합할 때 연관된 엔티티도 모두 병합하는것을 의미함.
    @JoinColumn(name = "user_id", updatable = false)
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private String date;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, orphanRemoval = true)
    private List<Image> image = new ArrayList<>();

    public void mapping(Member member) {
        this.member = member;
        member.getBoard().add(this);
    }
}
