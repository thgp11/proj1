package com.hospital.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name= "comment_likes",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"comment_id", "comment_member_id"})
        })
@Builder
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}