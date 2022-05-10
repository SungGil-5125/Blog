package com.project.blog.domain;

import lombok.*;

import javax.persistence.*;
import java.lang.reflect.Member;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long board_id;

    @ManyToOne(cascade = CascadeType.MERGE, targetEntity = Member.class) // merge란 엔티티 상태를 병합할 때 연관된 엔티티도 모두 병합하는것을 의미함.
    @JoinColumn(name = "member_id", updatable = false)
    private Member member;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String date;

    @Column
    private String img;
}
