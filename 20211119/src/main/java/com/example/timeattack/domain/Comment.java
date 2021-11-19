package com.example.timeattack.domain;

import com.example.timeattack.dto.CommentRequestDto;
import com.example.timeattack.repository.CommentRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long articleId;

    public Comment(Long articleId, CommentRequestDto requestDto) {
        this.articleId = articleId;
        this.content = requestDto.getContent();
    }
}
