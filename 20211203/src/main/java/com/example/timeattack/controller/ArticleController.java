package com.example.timeattack.controller;

import com.example.timeattack.domain.Article;
import com.example.timeattack.dto.ArticleRequestDto;
import com.example.timeattack.dto.CommentRequestDto;
import com.example.timeattack.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public void createArticle(@RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("tags") String tags,
                              @RequestParam("image") MultipartFile image) {
        articleService.createArticle(title, content, tags, image);
    }

    @GetMapping("/articles/{id}")
    public Article getArticle(@PathVariable("id") Long id) {
        return articleService.getArticle(id);
    }

    @PostMapping("/articles/{articleId}/comments")
    public void saveComment(@PathVariable("articleId") Long articleId, @RequestBody CommentRequestDto requestDto) {
        articleService.saveComment(articleId, requestDto);
    }
}
