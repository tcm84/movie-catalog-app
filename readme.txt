Techstack

Springboot
Spring REST
Java 8
Spock
Groovy
H2 embedded DB for testing and 
persistence (for demo only) - stored in a file in the app under /database
maven
lombok  - you need to install this with your IDE

To run integration tests
mvn clean verify or import as a maven project into your IDE and run the tests - your dev env needs to be correctly setup to run spock tests in your IDE

To build and run
mvn spring-boot:run - http://localhost:8080

Endpoints:

Movie directors:

1. (HTTP POST) To add a new movie directors details:          /moviedirectors/add
2. (HTTP POST) To update a new movie directors details:       /moviedirectors/update
3. (HTTP DELETE) To delete an existing movie directors details: /moviedirectors/delete/{moviedirectorId}

Movie ratings:

1. (HTTP POST) To add a new movie ratings details:          /movieratings/add
2. (HTTP POST) To update a new movie ratings details:       /movieratings/update
3. (HTTP DELETE) To delete an existing movie ratings details: /movieratings/delete/{movieratingId}

Movies:

These should be added with a movie director/rating id and not in isolation

1. (HTTP GET) Get a list of all a movie directors films from this catalog: /moviedirectors/{moviedirectorId}/movies/all

2. (HTTP POST) To add a new movie details for this movie director: /moviedirectors/{moviedirectorId}/movies/add
3. (HTTP POST) To update a movie details for this movie director:  /moviedirectors/{moviedirectorId}/movies/update
3. (HTTP POST) To add a new movie details for this movie rating:/movieratings/{movieratingId}/movies/add
4. (HTTP POST) To update a movie details for this movie rating:/movieratings/{movieratingId}/movies/update
5. (HTTP DELETE) To delete a movie: /movies/delete/{movieId}
6. (HTTP GET) To get a list of all movies with a movie rating >= movieClassification: /movieratings/movies/above/{movieClassification}
7. (HTTP GET) To get a list of all movies with a movie rating: /movieratings/{movieratingId}/movies/all

To log on to the H2 web console to experiment with persisting data between application restarts go to 

http://localhost:8080/console 

and logon as a server with the application name:  jdbc:h2:~/movie-catalog-app 
choose option Generic H2 Server from the first drop down list
use password and username:  test
