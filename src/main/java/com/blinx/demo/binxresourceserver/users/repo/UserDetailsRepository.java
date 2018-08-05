package com.blinx.demo.binxresourceserver.users.repo;

import org.springframework.data.repository.CrudRepository;

import com.blinx.demo.binxresourceserver.users.model.entities.UserDetails;

public interface UserDetailsRepository extends CrudRepository<UserDetails,String> {
}
