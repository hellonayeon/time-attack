package com.example.timeattack.repository;

import com.example.timeattack.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findByTag(String tag);
}
