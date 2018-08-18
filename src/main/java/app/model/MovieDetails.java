package app.model;

import java.util.Collection;
import java.util.ArrayList;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MovieDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int movieId;
	
	private String title;
	private String director;
	private String certificate;
	private String rating;
	private String gendre;
	private String releasedate;
	
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

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getGendre() {
		return gendre;
	}

	public void setGendre(String gendre) {
		this.gendre = gendre;
	}

	public String getReleasedate() {
		return releasedate;
	}

	public void setReleasedate(String releasedate) {
		this.releasedate = releasedate;
	}

	public Collection<String> getCast() {
		return cast;
	}

	public void setCast(Collection<String> cast) {
		this.cast = cast;
	}
}
