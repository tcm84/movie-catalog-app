package app.model;

import java.util.Collection;
import java.util.Date;
import java.util.ArrayList;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonFormat;

import app.enums.Genre;
import app.enums.Rating;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter 
@Setter
public class MovieDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int movieId;
	
	private String title;
	private String director;
	private Rating rating;
	private Genre  genre;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	private Date   releasedate;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Collection<String> cast = new ArrayList<>();
}