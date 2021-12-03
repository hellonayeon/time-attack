package com.example.timeattack.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequestDto {
    private String title;
    private String content;
    private String tags;
}
