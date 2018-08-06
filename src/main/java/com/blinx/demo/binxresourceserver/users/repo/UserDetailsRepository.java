package com.blinx.demo.binxresourceserver.users.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blinx.demo.binxresourceserver.users.model.entities.UserDetails;

@Repository
public interface UserDetailsRepository extends CrudRepository<UserDetails,String> {
	public UserDetails findByUserId(int userId);
	public Optional<UserDetails> findByUsernameAndEmailAddress(String username, String emailAddress);
}
