package com.moviecatalog.directors.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.moviecatalog.directors.enums.Nationality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name="directors")
/**
* The <code>DirectorDetails</code> class represents
* a director
*/
public class DirectorDetails {
	@Id
	@Column(name="director_id")
	private int directorId;
	
	@NotEmpty(message="Name must not be empty")
	@Column(name="name")
	private String name;
	
	@NotNull(message="Dob should not be null")
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd/MM/yy")
	@Column(name="dob")
	private Date dob;
	
	@Column(name="nationality")
	private Nationality nationality;
}