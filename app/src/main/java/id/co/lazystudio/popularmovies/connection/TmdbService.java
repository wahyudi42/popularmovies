package id.co.lazystudio.popularmovies.connection;

import id.co.lazystudio.popularmovies.BuildConfig;
import id.co.lazystudio.popularmovies.entity.Configuration;
import id.co.lazystudio.popularmovies.parser.MovieListParser;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
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
    @GET("movie/popular?api_key="+API_KEY)
    Call<MovieListParser> getPopular(@Query("page") int page);

    @Headers("Content-Type: application/json")
    @GET("movie/top_rated?api_key="+API_KEY)
    Call<MovieListParser> getTopRated(@Query("page") int page);
}
