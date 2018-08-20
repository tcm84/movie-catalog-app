package com.moviecatalog.directors.restcontrollers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moviecatalog.directors.model.DirectorDetails;
import com.moviecatalog.directors.services.DirectorService;

@RestController
@RequestMapping("/directors")
public class DirectorControllerImpl implements DirectorController {
	
	@Autowired
	private DirectorService directorsServiceImpl;
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/add")
	public DirectorDetails addDirector(@Valid @RequestBody DirectorDetails directorsDetails) {
		return  directorsServiceImpl.addDirector(directorsDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, value="/update")
	public DirectorDetails updateDirector(@Valid @RequestBody DirectorDetails directorDetails) {
		return directorsServiceImpl.updateDirector(directorDetails);
	}
	
	@Override
	@RequestMapping(method=RequestMethod.DELETE, value="/delete/{directorId}")
	public void deleteDirector(@PathVariable Integer directorId) {
		directorsServiceImpl.deleteDirector(directorId);
	}
}