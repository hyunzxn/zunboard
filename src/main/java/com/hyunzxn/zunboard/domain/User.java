package com.hyunzxn.zunboard.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String username;

	private String account;

	private String password;

	@OneToMany(mappedBy = "user")
	private List<Post> posts = new ArrayList<>();

	@Builder
	public User(String username, String account, String password) {
		this.username = username;
		this.account = account;
		this.password = password;
	}

	@Builder
	public User(String username, String account, String password, List<Post> posts) {
		this.username = username;
		this.account = account;
		this.password = password;
		this.posts = posts;
	}

	public void addPost(Post post) {
		posts.add(post);
		post.setUser(this);
	}
}
