package com.example.timeattack.service;

import com.example.timeattack.domain.Article;
import com.example.timeattack.domain.Comment;
import com.example.timeattack.domain.Tag;
import com.example.timeattack.dto.ArticleRequestDto;
import com.example.timeattack.dto.CommentRequestDto;
import com.example.timeattack.repository.ArticleRepository;
import com.example.timeattack.repository.CommentRepository;
import com.example.timeattack.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    public List<Article> getArticles(String searchTag) {
        if(searchTag.isEmpty()) return articleRepository.findAllByOrderByCreatedAtDesc();

        List<Tag> tags = tagRepository.findByNameOrderByCreatedAtDesc(searchTag);

        List<Article> articles = new ArrayList<>();
        for(Tag tag : tags) {
            articles.add(tag.getArticle());
        }
        return articles;
    }

    @Transactional
    public void createArticle(ArticleRequestDto requestDto) {
        articleRepository.save(new Article(requestDto));
    }

    public Article getArticle(Long id) {
        return articleRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당되는 아이디의 기사를 찾을 수 없습니다.")
        );
    }

    @Transactional
    public void saveComment(Long articleId, CommentRequestDto requestDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new NullPointerException("해당되는 아이디의 기사를 찾을 수 없습니다.")
        );
        commentRepository.save(new Comment(article, requestDto));
    }
}
