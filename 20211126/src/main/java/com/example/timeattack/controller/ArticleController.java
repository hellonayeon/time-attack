package com.example.timeattack.controller;

import com.example.timeattack.domain.Article;
import com.example.timeattack.dto.ArticleRequestDto;
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
    public List<Article> getArticles(@RequestParam("searchTag") String searchTag) {
        return articleService.getArticles(searchTag);
    }

    @PostMapping("/article")
    public void createArticle(@RequestBody ArticleRequestDto requestDto) {
        articleService.createArticle(requestDto);
    }
}
