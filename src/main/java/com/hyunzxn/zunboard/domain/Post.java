package com.hyunzxn.zunboard.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.hyunzxn.zunboard.request.PostUpdateRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	private String content;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public Post(String content) {
		this.content = content;
	}

	public static Post createPost(String content, User user) {
		Post post = Post.builder()
			.content(content)
			.build();
		post.setUser(user);
		return post;
	}

	public void setUser(User user) {
		this.user = user;
		user.getPosts().add(this);
	}

	public void update(PostUpdateRequest request) {
		this.content = request.getUpdatedContent();
	}
}
