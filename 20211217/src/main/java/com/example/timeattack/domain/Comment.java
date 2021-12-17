package com.example.timeattack.domain;

import com.example.timeattack.dto.request.CommentRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    public Comment(Article article, CommentRequestDto requestDto) {
        this.article = article;
        this.content = requestDto.getContent();
    }
}
