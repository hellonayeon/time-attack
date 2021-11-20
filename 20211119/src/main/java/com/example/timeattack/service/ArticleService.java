package com.example.timeattack.service;

import com.example.timeattack.domain.Article;
import com.example.timeattack.domain.ArticleComment;
import com.example.timeattack.dto.ArticleRequestDto;
import com.example.timeattack.dto.ArticleCommentRequestDto;
import com.example.timeattack.repository.ArticleRepository;
import com.example.timeattack.repository.ArticleCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository commentRepository;

    public List<Article> getArticles() {
        return articleRepository.findAllByOrderByIdDesc();
    }

    @Transactional
    public Long createArticle(ArticleRequestDto requestDto) {
        Article article = new Article(requestDto);
        articleRepository.save(article);
        return article.getId();
    }

    public Article getArticle(Long id) {
        return articleRepository.findById(id).orElseThrow(
            () -> new NullPointerException("해당되는 아이디의 기사를 찾을 수 없습니다.")
        );
    }

    @Transactional
    public void saveComment(Long articleId, ArticleCommentRequestDto requestDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new NullPointerException("해당되는 아이디의 기사를 찾을 수 없습니다.")
        );
        ArticleComment comment = new ArticleComment(article, requestDto);
        commentRepository.save(comment);
    }
}
