package com.example.timeattack.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Tag extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private String tag;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public Tag(String tag, Article article) {
        this.tag = tag;
        this.article = article;
    }
}
