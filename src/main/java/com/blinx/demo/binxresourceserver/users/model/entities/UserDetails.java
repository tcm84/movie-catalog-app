package com.blinx.demo.binxresourceserver.users.model.entities;

import java.util.Collection;
import java.util.ArrayList;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.blinx.demo.binxresourceserver.users.model.vo.Address;

@Entity
@Table(name="USER_DETAILS")
public class UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	
	@NotNull
	@Size(min=2, message="username must have at least 2 characters")
	private String username;
	
	private String password;
	
	private String company;
	
	@Email
	private String emailAddress;
	
	@Embedded
	private Address address;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="USER_PHONENUMBERS",
			   joinColumns=@JoinColumn(name = "USER_ID"))
	private Collection<String> phoneNumbers = new ArrayList<>();
	
	public int getUserId() {
		return userId;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
}
