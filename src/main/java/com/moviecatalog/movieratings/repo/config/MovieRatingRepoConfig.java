package com.moviecatalog.movieratings.repo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import com.moviecatalog.movieratings.models.MovieRatingDetails;

@Configuration
public class MovieRatingRepoConfig extends RepositoryRestConfigurerAdapter {
    @Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(MovieRatingDetails.class);
    }
}
