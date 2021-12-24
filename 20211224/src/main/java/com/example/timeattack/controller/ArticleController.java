package com.example.timeattack.controller;

import com.example.timeattack.domain.Article;
import com.example.timeattack.dto.request.CommentRequestDto;
import com.example.timeattack.security.UserDetailsImpl;
import com.example.timeattack.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/articles")
    public Page<Article> getArticles(@RequestParam("searchTag") String searchTag,
                                     @RequestParam("page") int page,
                                     @RequestParam("size") int size) {
        return articleService.getArticles(searchTag, page, size);
    }

    @PostMapping("/article")
    public void createArticle(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestParam("title") String title,
                              @RequestParam("content") String content,
                              @RequestParam("tags") String tags,
                              @Nullable @RequestParam("image") MultipartFile image) {
        articleService.createArticle(userDetails.getUser(), title, content, tags, image);
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
