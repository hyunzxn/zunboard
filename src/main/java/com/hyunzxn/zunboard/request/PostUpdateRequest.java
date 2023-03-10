package com.hyunzxn.zunboard.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {

	private String updatedContent;

	@Builder
	public PostUpdateRequest(String updatedContent) {
		this.updatedContent = updatedContent;
	}
}
