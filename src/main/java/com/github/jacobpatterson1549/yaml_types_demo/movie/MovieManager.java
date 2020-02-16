package com.github.jacobpatterson1549.yaml_types_demo.movie;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.jacobpatterson1549.yaml_types_demo.movie.type.ComedyMovie;

import java.io.IOException;
import java.io.InputStream;

/**
 * A utility to retrieve Movie from YAML files.
 */
public class MovieManager {
    /**
     * An ObjectMapper which deserializes YAML files into Movies.
     */
    private final ObjectMapper yamlMapper;

    /**
     * Creates a new MovieMapper with special Movie deserialization logic.
     */
    public MovieManager() {
        AnnotationIntrospector movieAnnotationIntrospector
            = new MovieAnnotationIntrospector();
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlMapper = new ObjectMapper(yamlFactory);
        yamlMapper.setAnnotationIntrospector(movieAnnotationIntrospector);
        yamlMapper.registerSubtypes(new NamedType(
            ComedyMovie.class, MovieGenre.COMEDY.getInternalName()));
    }

    /**
     * Gets a movie from a YAML InputStream.
     * 
     * @param yamlInputStream The InputStream to read the Movie from.
     * @return The Movie represented by the InputStream.
     * @throws IOException If a problem occurs reading the InputStream or
     * converting it to a Movie.
     */
    public Movie getMovieFromYaml(InputStream yamlInputStream)
        throws IOException {
        return yamlMapper.readValue(yamlInputStream, Movie.class);
    }

    private static class MovieAnnotationIntrospector
        extends JacksonAnnotationIntrospector {
        /**
         * The version of the AnnotationIntrospector.
         * Update after each modification
         */
        private static final long serialVersionUID = 1L;

        private static final TypeResolverBuilder<?> MOVIE_TYPE_RESOLVER_BUILDER
            = new StdTypeResolverBuilder()
                .init(Id.NAME, null)
                .inclusion(As.PROPERTY)
                .typeProperty("genre");

        /**
         * Sets Movie classes to deserialize on their [movie] genre.
         */
        @Override
        public TypeResolverBuilder<?> findTypeResolver(
            MapperConfig<?> config,
            AnnotatedClass ac,
            JavaType baseType) {
            if (ac.getAnnotated().equals(Movie.class)) {
                return MOVIE_TYPE_RESOLVER_BUILDER;
            }
            return super.findTypeResolver(config, ac, baseType);
        }
    }
}