package com.example.timeattack.controller;

import com.example.timeattack.domain.Article;
import com.example.timeattack.dto.ArticleDisplayRequestDto;
import com.example.timeattack.dto.ArticleRequestDto;
import com.example.timeattack.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public List<Article> getArticles() {
        return articleService.getArticles();
    }

    @PostMapping("/article")
    public Long createArticle(@RequestBody ArticleRequestDto requestDto) {
        return articleService.createArticle(requestDto);
    }

    @PutMapping("/article/{id}")
    public Long updateArticle(@PathVariable Long id, @RequestBody ArticleDisplayRequestDto requestDto) {
        return articleService.updateArticle(id, requestDto);
    }
}
