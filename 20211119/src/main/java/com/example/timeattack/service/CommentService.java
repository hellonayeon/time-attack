package com.example.timeattack.service;

import com.example.timeattack.domain.Comment;
import com.example.timeattack.dto.CommentRequestDto;
import com.example.timeattack.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public List<Comment> getComments(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }

    public Comment saveComment(Long articleId, CommentRequestDto requestDto) {
        Comment comment = new Comment(articleId, requestDto);
        return commentRepository.save(comment);
    }
}
