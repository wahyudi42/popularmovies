package id.co.lazystudio.watchIt_freemoviedatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.adapter.GenreAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.adapter.SummaryMovieAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbClient;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbService;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Genre;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.ErrorParser;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.GenreParser;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.MovieListParser;
import id.co.lazystudio.watchIt_freemoviedatabase.sync.WatchItSyncAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.view.MySliderView;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<Movie> mNowPlayingList = new ArrayList<>();
    private List<Movie> mPopularList = new ArrayList<>();
    private List<Movie> mTopRatedList = new ArrayList<>();
    private List<Genre> mGenres = new ArrayList<>();

    SliderLayout mNowPlayingSliderLayout;
    RelativeLayout nowPlayingRelativeLayout;

    List<String> processList = new ArrayList<>(Arrays.asList("now_playing", "genre", "popular", "top_rated"));

    RecyclerView mGenreRecyclerView, mPopularRecyclerView, mTopRatedRecyclerView;

    ProgressBar mainProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        mainProgressBar = (ProgressBar) findViewById(R.id.main_progressbar);

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(MainActivity.this).load(R.drawable.app_name).into((ImageView) findViewById(R.id.app_name));
            }
        });

        WatchItSyncAdapter.initializeSyncAdapter(this);

        nowPlayingRelativeLayout = (RelativeLayout) findViewById(R.id.now_playing_relative_layout);
        nowPlayingRelativeLayout.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(nowPlayingRelativeLayout.getWidth(), nowPlayingRelativeLayout.getWidth() * 9 / 16);
                nowPlayingRelativeLayout.setLayoutParams(params);
            }
        });

        mNowPlayingSliderLayout = (SliderLayout) findViewById(R.id.now_playing_slider);
        mNowPlayingSliderLayout.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mNowPlayingSliderLayout.getWidth(), mNowPlayingSliderLayout.getWidth() * 9 / 16);
                mNowPlayingSliderLayout.setLayoutParams(params);
            }
        });

        mGenreRecyclerView = (RecyclerView) findViewById(R.id.genre_recyclerview);

        mPopularRecyclerView = (RecyclerView) findViewById(R.id.popular_recyclerview);
        mPopularRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout parent = (LinearLayout) mPopularRecyclerView.getParent();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, parent.getWidth() / 2);
                mPopularRecyclerView.setLayoutParams(params);

                findViewById(R.id.popular_title_relativelayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("view all clicked", "popular");
                        viewAll(ListMovie.POPULAR);
                    }
                });
            }
        });

        mTopRatedRecyclerView = (RecyclerView) findViewById(R.id.toprated_recyclerview);
        mTopRatedRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout parent = (LinearLayout) mTopRatedRecyclerView.getParent();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, parent.getWidth() / 2);
                mTopRatedRecyclerView.setLayoutParams(params);

                findViewById(R.id.toprated_title_relativelayout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("view all clicked", "top rated");
                        viewAll(ListMovie.TOP_RATED);
                    }
                });
            }
        });

        getNowPlaying(this);
        getGenres(this);
        getPopular(this);
        getTopRated(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getNowPlaying(final Context context){
        final ProgressBar pb = (ProgressBar) findViewById(R.id.now_playing_progress_bar);
        if(Utils.isInternetConnected(context)) {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<MovieListParser> nowPlaying = tmdbService.getNowPlaying();

            nowPlaying.enqueue(new Callback<MovieListParser>() {
                @Override
                public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                    pb.setVisibility(View.GONE);
                    if(response.body().getStatusCode() != 0){
                        setComplete("now_playing", response.body());
                    }else{
                        setComplete("now_playing");
                        mNowPlayingList = response.body().getMovies();
                        populateNowPlaying(context);
                    }
                }

                @Override
                public void onFailure(Call<MovieListParser> call, Throwable t) {
                    setComplete("now_playing", "Server Error Occured");
                }
            });
        }else {
            setComplete("now_playing", "No Internet Connection");
        }
    }

    private void populateNowPlaying(final Context context){
        int to = 5;
        for(int i = 0; i <= to; i++){
            Movie nowPlaying = null;
            MySliderView textSliderView = null;
            if(i < to) {
                nowPlaying = mNowPlayingList.get(i);
                String imagePath = nowPlaying.getBackdropPath(context, 0);
                textSliderView = new MySliderView(context, i);
                textSliderView
                        .description(nowPlaying.getTitle())
                        .image(imagePath);
            }else{
                textSliderView = new MySliderView(context, -1);
                textSliderView.image(R.drawable.more_land);
            }
            textSliderView.error(R.drawable.no_image_land);
            textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    Integer index = (Integer) slider.getView().getTag();
                    if(index == -1){
                        viewAll(ListMovie.NOW_PLAYING);
                    }else{
                        Movie movie = mNowPlayingList.get(index);
                        Intent i = new Intent(context, DetailMovie.class);
                        i.putExtra(DetailMovie.MOVIE_KEY, movie);
                        startActivity(i);
                    }
                }
            });

            mNowPlayingSliderLayout.addSlider(textSliderView);
        }

        mNowPlayingSliderLayout.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
    }

    private void getGenres(final Context context){
        if(Utils.isInternetConnected(context)) {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<GenreParser> genre = tmdbService.getGenres();

            genre.enqueue(new Callback<GenreParser>() {
                @Override
                public void onResponse(Call<GenreParser> call, Response<GenreParser> response) {
                    if(response.body().getStatusCode() != 0){
                        setComplete("genre", response.body());
                    }else {
                        setComplete("genre");
                        mGenres = response.body().getGenres();
                        mGenreRecyclerView.setAdapter(new GenreAdapter(context, mGenres));
                    }
                }

                @Override
                public void onFailure(Call<GenreParser> call, Throwable t) {
                    setComplete("genre", "Server Error Occured");
                }
            });
        }else {
            setComplete("genre", "No Internet Connection");
        }
    }

    private void getPopular(final Context context){
        if(Utils.isInternetConnected(context)) {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<MovieListParser> popular = tmdbService.getPopular();

            popular.enqueue(new Callback<MovieListParser>() {
                @Override
                public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                    if(response.body().getStatusCode() != 0){
                        setComplete("popular", response.body());
                    }else {
                        setComplete("popular");
                        mPopularList = response.body().getMovies();
                        mPopularList.add(new Movie(-1));

                        mPopularRecyclerView.setAdapter(new SummaryMovieAdapter(context, mPopularList, ListMovie.POPULAR));
                    }
                }

                @Override
                public void onFailure(Call<MovieListParser> call, Throwable t) {
                    setComplete("popular", "Server Error Occured");
                }
            });
        }else {
            setComplete("popular", "No Internet Connection");
        }
    }

    private void getTopRated(final Context context){
        if(Utils.isInternetConnected(context)) {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<MovieListParser> topRated = tmdbService.getTopRated();

            topRated.enqueue(new Callback<MovieListParser>() {
                @Override
                public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                    if(response.body().getStatusCode() != 0){
                        setComplete("top_rated", response.body());
                    }else {
                        setComplete("top_rated");
                        mTopRatedList = response.body().getMovies();
                        mTopRatedList.add(new Movie(-1));

                        mTopRatedRecyclerView.setAdapter(new SummaryMovieAdapter(context, mTopRatedList, ListMovie.TOP_RATED));
                    }
                }

                @Override
                public void onFailure(Call<MovieListParser> call, Throwable t) {
                    setComplete("top_rated", "Server Error Occured");
                }
            });
        }else {
            setComplete("top_rated", "No Internet Connection");
        }
    }

    private void viewAll(String target){
        Intent i = new Intent(MainActivity.this, ListMovie.class);
        i.putExtra(target, true);
        startActivity(i);
    }

    private void setComplete(String process){
        Utils.setProcessComplete(mainProgressBar, processList, process);
    }

    private void setComplete(String process, String error){
        Utils.setProcessError(findViewById(R.id.error_textview), error);
        setComplete(process);
    }

    private void setComplete(String process, ErrorParser error){
        Utils.setProcessError(findViewById(R.id.error_textview), error);
        setComplete(process);
    }
}
