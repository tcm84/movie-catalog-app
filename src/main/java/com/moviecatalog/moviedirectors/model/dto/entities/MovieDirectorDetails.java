package com.moviecatalog.moviedirectors.model.dto.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moviecatalog.moviedirectors.enums.Nationality;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="movie_directors")
/**
* The <code>MovieDirectorDetails</code> class represents
* a movie director
*/
public class MovieDirectorDetails {
	@Id
	@Column(name="moviedirector_id")
	private int moviedirectorId;
	
	@NotEmpty(message="Name must not be empty")
	@Column(name="name")
	private String name;
	
	@NotNull(message="Dob should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yyyy")
	@Column(name="dob")
	private Date dob;
	
	@Column(name="nationality")
	private Nationality nationality;
}