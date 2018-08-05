package com.blinx.demo.binxresourceserver.model;

import java.util.List;

public class UserDetails {
	private String username;
	private String password;
	private String company;
	private Address address;
	private List<String> phoneNumbers;
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
	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}
	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}
	@Override
	public String toString() {
		return "UserDetails [username=" + username + ", password=" + password + ", company=" + company + ", address="
				+ address + ", phoneNumbers=" + phoneNumbers + "]";
	}
}
