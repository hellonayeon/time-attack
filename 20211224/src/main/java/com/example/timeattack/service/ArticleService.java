package com.example.timeattack.service;

import com.example.timeattack.domain.Article;
import com.example.timeattack.domain.Comment;
import com.example.timeattack.domain.Tag;
import com.example.timeattack.domain.User;
import com.example.timeattack.dto.request.CommentRequestDto;
import com.example.timeattack.repository.ArticleRepository;
import com.example.timeattack.repository.CommentRepository;
import com.example.timeattack.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    private final FileProcessService fileProcessService;

    public Page<Article> getArticles(String searchTag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if(!searchTag.isEmpty()) return articleRepository.findAllByTagsName(searchTag, pageable);

        return articleRepository.findAll(pageable);
    }

    @Transactional
    public void createArticle(User user, String title, String content, String tags, MultipartFile image) {
        if(image != null) {
            String imageUrl = fileProcessService.uploadImage(image);
            articleRepository.save(new Article(title, content, tags, imageUrl, user));
        }
        else {
            articleRepository.save(new Article(title, content, tags, user));
        }
    }

    public Article getArticle(Long id) {
        return articleRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당되는 아이디의 기사를 찾을 수 없습니다.")
        );
    }

    @Transactional
    public void saveComment(Long articleId, CommentRequestDto requestDto) {
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new NullPointerException("해당되는 아이디의 기사를 찾을 수 없습니다.")
        );
        commentRepository.save(new Comment(article, requestDto));
    }
}
