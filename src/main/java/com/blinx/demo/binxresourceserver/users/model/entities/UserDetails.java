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
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	public Collection<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(Collection<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	@Override
	public String toString() {
		return "UserDetails [username=" + username + ", password=" + password + ", company=" + company + ", address="
				+ address + ", phoneNumbers=" + phoneNumbers + "]";
	}
}
