package com.github.jacobpatterson1549.yaml_types_demo.movie;

/**
 * A video that can be shown to an audience in a theater.
 */
public interface Movie {
    /**
     * Gets the title of the Movie
     * 
     * @return The title of the Movie.
     */
    String getTitle();

    /**
     * Gets the MovieGenre of the Movie.
     * 
     * @return The MovieGenre of the Movie.
     */
    MovieGenre getMovieGenre();
}