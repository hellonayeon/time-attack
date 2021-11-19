package com.example.timeattack.controller;

import com.example.timeattack.domain.Comment;
import com.example.timeattack.dto.CommentRequestDto;
import com.example.timeattack.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/articles/{articleId}/comments")
    public List<Comment> getComments(@PathVariable("articleId") Long articleId) {
        return commentService.getComments(articleId);
    }

    @PostMapping("/articles/{articleId}/comments")
    public Comment saveComment(@PathVariable("articleId") Long articleId, @RequestBody CommentRequestDto commentRequestDto) {
        return commentService.saveComment(articleId, commentRequestDto);
    }
}
