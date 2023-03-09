package com.hyunzxn.zunboard.response;

import java.time.LocalDateTime;

import com.hyunzxn.zunboard.domain.Post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

	private final Long postId;
	private final String content;
	private final LocalDateTime createdAt;
	private final LocalDateTime lastModifiedAt;
	private final String createdBy;

	@Builder
	public PostResponse(Long postId, String content, LocalDateTime createdAt, LocalDateTime lastModifiedAt,
		String createdBy) {
		this.postId = postId;
		this.content = content;
		this.createdAt = createdAt;
		this.lastModifiedAt = lastModifiedAt;
		this.createdBy = createdBy;
	}

	public static PostResponse changeToDto(Post post) {
		return PostResponse.builder()
			.postId(post.getId())
			.content(post.getContent())
			.createdAt(post.getCreatedAt())
			.lastModifiedAt(post.getModifiedAt())
			.createdBy(post.getUser().getAccount())
			.build();
	}
}
