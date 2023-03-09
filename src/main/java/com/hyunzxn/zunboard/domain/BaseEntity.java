package com.hyunzxn.zunboard.domain;

import java.time.LocalDateTime;

import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class BaseEntity {

	@CreatedDate
	private LocalDateTime createdAt;

	@LastModifiedDate
	private LocalDateTime modifiedAt;
}
