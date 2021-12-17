package com.example.timeattack.domain;

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

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "article")
    private List<Comment> comments;

    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<Tag> tags = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Article(String title, String content, String tags, String imageUrl, User user) {
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        String[] tagNames = tags.split(",");
        for(String name : tagNames) {
            this.tags.add(new Tag(name, this));
        }
        this.user = user;
    }

    public Article(String title, String content, String tags, User user) {
        this.title = title;
        this.content = content;
        String[] tagNames = tags.split(",");
        for(String name : tagNames) {
            this.tags.add(new Tag(name, this));
        }
        this.user = user;
    }
}
