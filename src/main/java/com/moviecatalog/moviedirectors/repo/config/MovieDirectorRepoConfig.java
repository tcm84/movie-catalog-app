package com.moviecatalog.moviedirectors.repo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.moviecatalog.moviedirectors.model.MovieDirectorDetails;

@Configuration
public class MovieDirectorRepoConfig extends RepositoryRestConfigurerAdapter {
    @Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(MovieDirectorDetails.class);
    }
}
