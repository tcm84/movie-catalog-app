package com.moviecatalog.movies.model;

import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moviecatalog.movies.enums.Genre;
import com.moviecatalog.movies.enums.Rating;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter 
@Setter
@Table(name="movie_catalog")
public class MovieDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="movie_id")
	private int movieId;
	
	@NotEmpty(message="Movie title must not be empty")
	private String title;
	
	@NotEmpty(message="Director must not be empty")
	private String director;
	
	private Rating rating;	
	private Genre  genre;
	
	@NotNull(message="Releasedate should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	@Column(name="release_date")
	private Date   releasedate;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@Size(min=1, message="Cast must contains at least one actor")
	private Collection<String> cast = new ArrayList<>();
}
