package com.moviecatalog.movieratings.model.dto.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.moviecatalog.movieratings.model.dto.vo.MovieWarning;
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
	
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="movie_warnings",
	   joinColumns=@JoinColumn(name = "movierating_id"))
	private Collection<MovieWarning> moviewarnings = new ArrayList<>();
}
