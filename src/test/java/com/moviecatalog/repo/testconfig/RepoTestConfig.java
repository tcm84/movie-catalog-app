package com.moviecatalog.repo.testconfig;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import com.moviecatalog.moviedirectors.model.dto.entities.MovieDirectorDetails;
import com.moviecatalog.moviedirectors.repo.MovieDirectorRepository;
import com.moviecatalog.movieratings.model.dto.entities.MovieRatingDetails;
import com.moviecatalog.movieratings.repo.MovieRatingRepository;
import com.moviecatalog.movies.model.dto.entities.MovieDetails;
import com.moviecatalog.movies.repo.MovieRepository;

@TestConfiguration
@EnableJpaRepositories(basePackageClasses = {MovieDirectorRepository.class,MovieRepository.class,
		MovieRatingRepository.class})
public class RepoTestConfig {
	@Bean
	public DataSource dataSource() {
		EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
		return builder.setType(EmbeddedDatabaseType.H2).build();
	}

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);
		vendorAdapter.setShowSql(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan(MovieDirectorDetails.class.getPackage().getName(),
								  MovieDetails.class.getPackage().getName(),
								  MovieRatingDetails.class.getPackage().getName());
		factory.setDataSource(dataSource());
		factory.afterPropertiesSet();

		return factory.getObject();
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory());
		return txManager;
	}
}
