package com.hyunzxn.zunboard.domain;

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

	private String title;

	@Lob
	private String content;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@Builder
	public Post(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public static Post createPost(String title, String content, User user) {
		Post post = Post.builder()
			.title(title)
			.content(content)
			.build();
		post.setUser(user);
		return post;
	}

	public void setIdForTest(Long id) {
		this.id = id;
	} //TODO 이 부분이 마음에 좀 걸린다. 이렇게 하는게 맞나?

	public void setUser(User user) {
		this.user = user;
		user.getPosts().add(this);
	}

	public void update(PostUpdateRequest request) {
		this.title = request.getUpdatedTitle();
		this.content = request.getUpdatedContent();
	}
}
