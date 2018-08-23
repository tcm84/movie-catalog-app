Techstack

Springboot
Spring REST
Java 8
Spock
Groovy
H2 embedded DB for testing and 
persistence (for demo only) - stored in a file in the app under /database
maven
lombok 


To build
mvn install - runs test cases too

To run
mvn spring-boot:run - http://localhost:8080

Endpoints:

Movie directors:

1. To add a new movie directors details:          /moviedirectors/add
2. To update a new movie directors details:       /moviedirectors/update
3. To delete an existing movie directors details: /moviedirectors/delete/{moviedirectorId}

Movie ratings:

1. To add a new movie ratings details:          /movieratings/add
2. To update a new movie ratings details:       /movieratings/update
3. To delete an existing movie ratings details: /movieratings/delete/{movieratingId}

Movies:

These should be added with a movie director/rating id and not in isolation

1. Get a list of all a movie directors films from this catalog: /moviedirectors/{moviedirectorId}/movies/all

2. To add a new movie details for this movie director: /moviedirectors/{moviedirectorId}/movies/add
3. To update a movie details for this movie director:  /moviedirectors/{moviedirectorId}/movies/update
3. To add a new movie details for this movie rating:/movieratings/{movieratingId}/movies/add
4. To update a movie details for this movie rating:/movieratings/{movieratingId}/movies/update
5. To delete a movie: /movies/delete/{movieId}
6. To get a list of all movies with a movie rating >= movieClassification: /movieratings/movies/above/{movieClassification}


To log on to the H2 web console to experiment with persisting data between application restarts go to 

http://localhost:8080/console 

and logon as a server with the application name:  jdbc:h2:~/movie-catalog-app 
choose option Generic H2 Server from the first drop down list
use password and username:  test
