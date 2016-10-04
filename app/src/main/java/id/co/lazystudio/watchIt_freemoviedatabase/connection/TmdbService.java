package id.co.lazystudio.watchIt_freemoviedatabase.connection;

import id.co.lazystudio.watchIt_freemoviedatabase.BuildConfig;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Configuration;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.GenreParser;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.MovieListParser;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.MovieParser;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by faqiharifian on 23/09/16.
 */
public interface TmdbService {
    String API_KEY = BuildConfig.TMDB_API_KEY;

    @Headers("Content-Type: application/json")
    @GET("configuration?api_key="+API_KEY)
    Call<Configuration> getConfiguration();

    @Headers("Content-Type: application/json")
    @GET("genre/movie/list?api_key="+API_KEY)
    Call<GenreParser> getGenres();

    @Headers("Content-Type: application/json")
    @GET("movie/now_playing?api_key="+API_KEY)
    Call<MovieListParser> getNowPlaying();

    @Headers("Content-Type: application/json")
    @GET("movie/now_playing?api_key="+API_KEY)
    Call<MovieListParser> getNowPlaying(@Query("page") int page);

    @Headers("Content-Type: application/json")
    @GET("movie/popular?api_key="+API_KEY)
    Call<MovieListParser> getPopular();


    @Headers("Content-Type: application/json")
    @GET("movie/popular?api_key="+API_KEY)
    Call<MovieListParser> getPopular(@Query("page") int page);

    @Headers("Content-Type: application/json")
    @GET("movie/top_rated?api_key="+API_KEY)
    Call<MovieListParser> getTopRated();

    @Headers("Content-Type: application/json")
    @GET("movie/top_rated?api_key="+API_KEY)
    Call<MovieListParser> getTopRated(@Query("page") int page);

    @Headers("Content-Type: application/json")
    @GET("movie/{id}?api_key="+API_KEY+"&append_to_response=images,videos,keywords,similar")
    Call<MovieParser> getMovie(@Path("id") int id);

    @Headers("Content-Type: application/json")
    @GET("genre/{genre_id}/movies?api_key="+API_KEY)
    Call<MovieListParser> getMoviesGenre(@Path("genre_id") int idGenre);

    @Headers("Content-Type: application/json")
    @GET("genre/{genre_id}/movies?api_key="+API_KEY)
    Call<MovieListParser> getMoviesGenre(@Path("genre_id") int idGenre, @Query("page") int page);

    @Headers("Content-Type: application/json")
    @GET("collection/{collection_id}?api_key="+API_KEY)
    Call<MovieListParser> getMoviesCollection(@Path("collection_id") int idCollection);

    @Headers("Content-Type: application/json")
    @GET("keyword/{keyword_id}/movies?api_key="+API_KEY)
    Call<MovieListParser> getMoviesKeyword(@Path("keyword_id") int idKeyword);

    @Headers("Content-Type: application/json")
    @GET("keyword/{keyword_id}/movies?api_key="+API_KEY)
    Call<MovieListParser> getMoviesKeyword(@Path("keyword_id") int idKeyword, @Query("page") int page);

    @Headers("Content-Type: application/json")
    @GET("movie/{movie_id}/similar?api_key="+API_KEY)
    Call<MovieListParser> getMoviesSimilar(@Path("movie_id") int idMovie);

    @Headers("Content-Type: application/json")
    @GET("movie/{movie_id}/similar?api_key="+API_KEY)
    Call<MovieListParser> getMoviesSimilar(@Path("movie_id") int idMovie, @Query("page") int page);
}
