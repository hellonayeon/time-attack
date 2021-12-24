package com.example.timeattack.repository;

import com.example.timeattack.domain.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAll(Pageable pageable);
    Page<Article> findAllByTagsName(String searchTag, Pageable pageable);
}
