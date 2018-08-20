package com.moviecatalog.movies.repo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.moviecatalog.movies.model.dto.entities.MovieDetails;

@Configuration
public class MovieRepoConfig extends RepositoryRestConfigurerAdapter {
    @Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(MovieDetails.class);
    }
}
