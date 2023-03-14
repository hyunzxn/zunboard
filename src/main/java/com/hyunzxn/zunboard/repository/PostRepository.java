package com.hyunzxn.zunboard.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hyunzxn.zunboard.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

	Page<Post> findAllByOrderByIdDesc(Pageable pageable);

	// @Override
	// @Query("select p from Post p join fetch p.user where p.id =:id")
	// Optional<Post> findById(@Param("id") Long id);

	@Override
	@EntityGraph(attributePaths = {"user"})
	Optional<Post> findById(Long id);
}
