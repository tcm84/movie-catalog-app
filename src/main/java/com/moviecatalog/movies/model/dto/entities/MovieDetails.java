package com.moviecatalog.movies.model.dto.entities;

import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moviecatalog.moviedirectors.model.dto.entities.MovieDirectorDetails;
import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;
import com.moviecatalog.movies.enums.Genre;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter 
@Setter
@Table(name="movies")
/**
* The <code>MovieDetails</code> class represents
* a movie
*/
public class MovieDetails {
	@Id
	@Column(name="movie_id")
	private int movieId;
	
	@NotEmpty(message="Movie title must not be empty")
	@Column(name="title")
	private String title;
	
	@ManyToOne
	private MovieDirectorDetails movieDirectorDetails;
	
	@ManyToOne
	private MovieRatingDetails movieRatingDetails;
	
	@Column(name="genre")
	private Genre  genre;
	
	@NotNull(message="Releasedate should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	@Column(name="release_date")
	private Date   releasedate;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@Size(min=1, message="Cast must contains at least one actor")
	@JoinTable(name="movie_cast",
	   joinColumns=@JoinColumn(name = "movie_id"))
	private Collection<String> cast = new ArrayList<>();
}
