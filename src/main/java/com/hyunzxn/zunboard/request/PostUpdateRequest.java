package com.hyunzxn.zunboard.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostUpdateRequest {

	private String updatedTitle;
	private String updatedContent;

	@Builder
	public PostUpdateRequest(String updatedTitle, String updatedContent) {
		this.updatedTitle = updatedTitle;
		this.updatedContent = updatedContent;
	}
}
