package com.example.timeattack.service;

import com.example.timeattack.domain.Article;
import com.example.timeattack.domain.Tag;
import com.example.timeattack.dto.ArticleRequestDto;
import com.example.timeattack.repository.ArticleRepository;
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

    public List<Article> getArticles(String searchTag) {
        if(searchTag.equals("")) return articleRepository.findAllByOrderByIdDesc();

        List<Tag> tagList = tagRepository.findByTag(searchTag);

        List<Article> articleList = new ArrayList<>();
        for(Tag tag : tagList) {
            articleList.add(tag.getArticle());
        }
        return articleList;
    }

    @Transactional
    public void createArticle(ArticleRequestDto requestDto) {
        Article article = new Article(requestDto);
        articleRepository.save(article);
    }
}
