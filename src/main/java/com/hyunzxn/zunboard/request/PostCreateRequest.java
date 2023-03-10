package com.hyunzxn.zunboard.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

	private String title;
	private String content;

	@Builder
	public PostCreateRequest(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
