package com.example.timeattack.domain;

import com.example.timeattack.dto.ArticleCommentRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ArticleComment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public ArticleComment(Article article, ArticleCommentRequestDto requestDto) {
        this.article = article;
        this.content = requestDto.getContent();
    }
}
