package com.dan.api_example.repository;

import com.dan.api_example.common.entity.BaseEntity;
import com.dan.api_example.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findByIdAndState(Long id, BaseEntity.State state);
}
