package com.moviecatalog.directors.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moviecatalog.directors.exceptions.DirectorExistsException;
import com.moviecatalog.directors.exceptions.DirectorNotFoundException;
import com.moviecatalog.directors.model.DirectorDetails;
import com.moviecatalog.directors.repo.DirectorRepository;

@Service
public class DirectorServiceImpl implements DirectorService {

	@Autowired
	private DirectorRepository directorRepository;
	
	@Override
	public DirectorDetails addDirector(DirectorDetails directorDetails) {
		if(directorRepository.existsById(directorDetails.getDirectorId())) {
			throw new DirectorExistsException();
		}
		return directorRepository.save(directorDetails);
	}
	
	@Override
	public DirectorDetails updateDirector(DirectorDetails directorDetails) {
		if(!directorRepository.existsById(directorDetails.getDirectorId())) {
			throw new DirectorNotFoundException();
		}
		return directorRepository.save(directorDetails);
	}
	
	@Override
	public void deleteDirector(Integer directorId) {
		directorRepository.deleteById(directorId);
	}
}