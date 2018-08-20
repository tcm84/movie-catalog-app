package com.moviecatalog.directors.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalog.directors.model.DirectorDetails;
import com.moviecatalog.directors.repo.DirectorRepository;

@Service
public class DirectorServiceImpl implements DirectorService {

	@Autowired
	private DirectorRepository directorRepository;
	
	@Override
	public DirectorDetails addDirector(DirectorDetails directorDetails) {
		return directorRepository.save(directorDetails);
	}
}
