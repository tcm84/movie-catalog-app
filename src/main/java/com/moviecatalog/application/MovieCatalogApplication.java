package com.moviecatalog.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.moviecatalog.application.MovieCatalogApplication;

@SpringBootApplication
@ComponentScan(basePackages= {"com.moviecatalog.movies"})
public class MovieCatalogApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieCatalogApplication.class, args);
	}
}
