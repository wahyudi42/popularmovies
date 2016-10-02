package id.co.lazystudio.watchIt_freemoviedatabase;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.adapter.ListMovieAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbClient;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbService;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.MovieListParser;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListMovie extends AppCompatActivity {
    public static final String NOW_PLAYING = "now_playing";
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String GENRE = "genre";
    public static final String COLLECTION = "collection";
    public static final String KEYWORD = "keyword";
    public static final String SIMILAR = "similar";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";

    List<Movie> mMovieList = new ArrayList<>();
    private ProgressBar progressBar;
    private GridView listMovieGridView;
    private String mType;
    private int mId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.list_movie_progressbar);
        Bundle args = getIntent().getExtras();
        if(args.getBoolean(NOW_PLAYING, false)) {
            getSupportActionBar().setTitle(getResources().getString(R.string.now_playing_title));
            mType = NOW_PLAYING;
        }
        if(args.getBoolean(POPULAR, false)) {
            getSupportActionBar().setTitle(getResources().getString(R.string.popular_title));
            mType = POPULAR;
        }
        if(args.getBoolean(TOP_RATED, false)) {
            getSupportActionBar().setTitle(getResources().getString(R.string.toprated_title));
            mType = TOP_RATED;
        }
        if(args.getBoolean(GENRE, false)) {
            String title = args.getString(KEY_TITLE);
            getSupportActionBar().setTitle(title);
            mId = args.getInt(KEY_ID);
            mType = GENRE;
        }
        if(args.getBoolean(KEYWORD, false)) {
            String title = args.getString(KEY_TITLE);
            getSupportActionBar().setTitle(title);
            mId = args.getInt(KEY_ID);
            mType = KEYWORD;
        }
        if(args.getBoolean(COLLECTION, false)) {
            String title = args.getString(KEY_TITLE);
            getSupportActionBar().setTitle(title);
            mId = args.getInt(KEY_ID);
            mType = COLLECTION;
        }
        if(args.getBoolean(SIMILAR, false)) {
            String title = getResources().getString(R.string.list_movie_similar_title)+" "+args.getString(KEY_TITLE);
            getSupportActionBar().setTitle(title);
            mId = args.getInt(KEY_ID);
            mType = SIMILAR;
        }

        listMovieGridView = (GridView) findViewById(R.id.movies_gridview);

        // Instance of ImageAdapter Class
        listMovieGridView.setAdapter(new ListMovieAdapter(this, mMovieList));
        getMovieList(this);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void getMovieList(final Context context){
        if(Utils.isInternetConnected(context)) {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            Call<MovieListParser> movieListCall;
            switch (mType){
                case POPULAR:
                    movieListCall = tmdbService.getPopular();
                    break;
                case TOP_RATED:
                    movieListCall = tmdbService.getTopRated();
                    break;
                case GENRE:
                    movieListCall = tmdbService.getMoviesGenre(mId);
                    break;
                case COLLECTION:
                    movieListCall = tmdbService.getMoviesCollection(mId);
                    break;
                case KEYWORD:
                    movieListCall = tmdbService.getMoviesKeyword(mId);
                    break;
                case SIMILAR:
                    movieListCall = tmdbService.getMoviesSimilar(mId);
                    break;
                default:
                    movieListCall = tmdbService.getNowPlaying();
                    break;
            }

            movieListCall.enqueue(new Callback<MovieListParser>() {
                @Override
                public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                    progressBar.setVisibility(View.GONE);
                    mMovieList = response.body().getMovies();
                    listMovieGridView.setAdapter(new ListMovieAdapter(context, mMovieList));
                }

                @Override
                public void onFailure(Call<MovieListParser> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
