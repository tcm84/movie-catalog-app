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

import com.blinx.demo.binxresourceserver.users.model.vo.Address;

import lombok.Data;

@Data
@Entity
@Table(name="USER_DETAILS")
public class UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userId;
	private String username;
	private String password;
	private String company;
	
	@Embedded
	private Address address;
	
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="USER_PHONENUMBERS",
			   joinColumns=@JoinColumn(name = "USER_ID"))
	private Collection<String> phoneNumbers = new ArrayList<>();
}
