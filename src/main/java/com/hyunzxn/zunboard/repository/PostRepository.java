package com.hyunzxn.zunboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunzxn.zunboard.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
