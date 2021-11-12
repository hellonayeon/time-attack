package com.example.timeattack.domain;

import com.example.timeattack.dto.ArticleRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Article {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean display;

    public Article(ArticleRequestDto requestDto) {
        this.content = requestDto.getContent();
        this.display = false;
    }

    public void updateDisplay(boolean display) {
        this.display = display;
    }
}
