package com.example.timeattack.service;

import com.example.timeattack.domain.Article;
import com.example.timeattack.dto.ArticleDisplayRequestDto;
import com.example.timeattack.dto.ArticleRequestDto;
import com.example.timeattack.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> getArticles() {
        return articleRepository.findAllByDisplay(true);
    }

    @Transactional
    public Long createArticle(ArticleRequestDto requestDto) {
        Article article = new Article(requestDto);
        articleRepository.save(article);
        return article.getId();
    }

    @Transactional
    public Long updateArticle(Long id, ArticleDisplayRequestDto requestDto) {
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 아이디가 존재하지 않습니다.")
        );
        article.updateDisplay(requestDto.isDisplay());
        return article.getId();
    }
}
