package com.example.timeattack.controller;

import com.example.timeattack.domain.Article;
import com.example.timeattack.dto.ArticleRequestDto;
import com.example.timeattack.dto.ArticleCommentRequestDto;
import com.example.timeattack.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342", maxAge = 3600)
@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public List<Article> getArticles() {
        return articleService.getArticles();
    }

    @PostMapping("/articles")
    public Long createArticle(@RequestBody ArticleRequestDto requestDto) {
        return articleService.createArticle(requestDto);
    }

    @GetMapping("/articles/{id}")
    public Article getArticle(@PathVariable("id") Long id) {
        return articleService.getArticle(id);
    }

    @PostMapping("/articles/{articleId}/comments")
    public void saveComment(@PathVariable("articleId") Long articleId, @RequestBody ArticleCommentRequestDto commentRequestDto) {
        articleService.saveComment(articleId, commentRequestDto);
    }
}
