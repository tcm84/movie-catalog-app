package com.moviecatalog.directors.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="directors")
/**
* The <code>DirectorDetails</code> class represents
* a director
*/
public class DirectorDetails {
	@Id
	@Column(name="director_id")
	private int directorId;
	
	private String name;
}
