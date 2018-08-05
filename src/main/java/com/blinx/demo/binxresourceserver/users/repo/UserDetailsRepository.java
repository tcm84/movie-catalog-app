package com.blinx.demo.binxresourceserver.users.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blinx.demo.binxresourceserver.users.model.entities.UserDetails;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails,String> {
	public UserDetails findByUserId(int userId);
}
