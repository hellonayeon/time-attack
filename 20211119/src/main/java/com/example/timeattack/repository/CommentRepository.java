package com.example.timeattack.repository;

import com.example.timeattack.domain.Article;
import com.example.timeattack.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    public List<Comment> findAllByArticleId(Long articleId);
}
