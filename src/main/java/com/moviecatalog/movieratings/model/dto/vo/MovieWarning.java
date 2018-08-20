package com.moviecatalog.movieratings.model.dto.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class MovieWarning {
	@Column(name="title")
	private String title;
	@Column(name="summary")
	private String summary;
}