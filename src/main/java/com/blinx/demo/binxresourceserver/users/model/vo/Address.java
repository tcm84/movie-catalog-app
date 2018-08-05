package com.blinx.demo.binxresourceserver.users.model.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Address {
	@Column(name="STREET_ADDR")
	private String streetAddress;
	@Column(name="CITY_NAME")
	private String city;
	@Column(name="POST_CODE")
	private String postCode;
	
	public String getStreetAddress() {
		return streetAddress;
	}
	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	@Override
	public String toString() {
		return "Address [streetAddress=" + streetAddress + ", city=" + city + ", postCode=" + postCode + "]";
	}
}
