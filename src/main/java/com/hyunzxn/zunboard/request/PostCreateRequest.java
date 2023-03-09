package com.hyunzxn.zunboard.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostCreateRequest {

	private String content;

	@Builder
	public PostCreateRequest(String content) {
		this.content = content;
	}
}
