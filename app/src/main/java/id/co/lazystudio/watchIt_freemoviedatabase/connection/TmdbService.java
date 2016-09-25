package id.co.lazystudio.watchIt_freemoviedatabase.connection;

import id.co.lazystudio.watchIt_freemoviedatabase.BuildConfig;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Configuration;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.GenreResponse;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.NowPlayingResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

/**
 * Created by faqiharifian on 23/09/16.
 */
public interface TmdbService {
    String API_KEY = BuildConfig.TMDB_API_KEY;

    @Headers("Content-Type: application/json")
    @GET("configuration?api_key="+API_KEY)
    Call<Configuration> getConfiguration();

    @Headers("Content-Type: application/json")
    @GET("movie/now_playing?api_key="+API_KEY)
    Call<NowPlayingResponse> getNowPlaying();

    @Headers("Content-Type: application/json")
    @GET("genre/movie/list?api_key="+API_KEY)
    Call<GenreResponse> getGenres();
}
