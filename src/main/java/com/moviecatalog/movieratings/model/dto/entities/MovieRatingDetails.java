package com.moviecatalog.movieratings.model.dto.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.moviecatalog.movies.enums.MovieClassification;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="movie_ratings")
/**
* The <code>MovieRatingDetails</code> class represents
* a movie rating
*/
public class MovieRatingDetails {
	@Id
	@Column(name="movierating_id")
	private int movieratingId; 
	
	@Column(name="movieclassification")
	private MovieClassification movieClassification;
	
	@NotEmpty(message="Description should not be empty")
	@Column(name="description")
	private String description;
}
