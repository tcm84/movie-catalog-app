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

@Entity
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}
	
	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public Date getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(Date releasedate) {
		this.releasedate = releasedate;
	}

	public Collection<String> getCast() {
		return cast;
	}

	public void setCast(Collection<String> cast) {
		this.cast = cast;
	}
}
