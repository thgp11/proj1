package com.hospital.repository;

import com.hospital.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByComment_IdAndMember_Id(Long commentId, Long commentUserId);
}
