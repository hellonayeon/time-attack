package com.example.timeattack.domain;

import com.example.timeattack.dto.ArticleRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Article extends Timestamped {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    public Article(ArticleRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        String[] tags = requestDto.getTags().split(",");
        for(String tag : tags) {
            this.tags.add(new Tag(tag, this));
        }
    }
}
