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
    @GET("movie/popular?api_key="+API_KEY)
    Call<MovieListParser> getPopular();

    @Headers("Content-Type: application/json")
    @GET("movie/top_rated?api_key="+API_KEY)
    Call<MovieListParser> getTopRated();

    @Headers("Content-Type: application/json")
    @GET("movie/{id}?api_key="+API_KEY+"&append_to_response=images,videos,keywords,similar")
    Call<MovieParser> getMovie(@Path("id") int id);
}
