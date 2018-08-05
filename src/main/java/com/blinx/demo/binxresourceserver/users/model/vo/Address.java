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
}
