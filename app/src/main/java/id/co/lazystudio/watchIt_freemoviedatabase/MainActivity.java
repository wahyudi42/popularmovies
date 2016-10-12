package id.co.lazystudio.watchIt_freemoviedatabase;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.adapter.GenreAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.adapter.SummaryMovieAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbClient;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbService;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Genre;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.GenreParser;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.MovieListParser;
import id.co.lazystudio.watchIt_freemoviedatabase.sync.WatchItSyncAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.FabVisibilityChangeListener;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.Utils;
import id.co.lazystudio.watchIt_freemoviedatabase.view.MySliderView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<Movie> mNowPlayingList = new ArrayList<>();
    private List<Movie> mPopularList = new ArrayList<>();
    private List<Movie> mTopRatedList = new ArrayList<>();
    private List<Genre> mGenres = new ArrayList<>();

    SliderLayout mNowPlayingSliderLayout;
    RelativeLayout mNowPlayingRelativeLayout;

    List<String> processList = new ArrayList<>();

    RecyclerView mGenreRecyclerView, mPopularRecyclerView, mTopRatedRecyclerView;

    ProgressBar mainProgressBar;

    TextView mNotificationTextView;

    View contentContainer;

    FloatingActionButton refreshFab;
    FabVisibilityChangeListener fabListener;

    boolean isSuccess = true;

    private AdView mAdView;

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

        mNotificationTextView = (TextView) findViewById(R.id.notification_textview);

        toolbar.post(new Runnable() {
            @Override
            public void run() {
                Picasso.with(MainActivity.this).load(R.drawable.app_name).into((ImageView) findViewById(R.id.app_name));
            }
        });

        WatchItSyncAdapter.initializeSyncAdapter(this);

        mNowPlayingRelativeLayout = (RelativeLayout) findViewById(R.id.now_playing_relative_layout);

        mNowPlayingSliderLayout = (SliderLayout) findViewById(R.id.now_playing_slider);

        mGenreRecyclerView = (RecyclerView) findViewById(R.id.genre_recyclerview);

        contentContainer = findViewById(R.id.content_container);

        mPopularRecyclerView = (RecyclerView) findViewById(R.id.popular_recyclerview);

        mTopRatedRecyclerView = (RecyclerView) findViewById(R.id.toprated_recyclerview);

        refreshFab = (FloatingActionButton) findViewById(R.id.refresh_fab);
        fabListener = new FabVisibilityChangeListener();

        getNowPlaying(this);
    }

    private void getNowPlaying(final Context context){
        final ProgressBar pb = (ProgressBar) findViewById(R.id.now_playing_progress_bar);
        if(Utils.isInternetConnected(context)) {
            Utils.initializeAd(this, contentContainer);

            pb.setVisibility(View.VISIBLE);

            mNowPlayingRelativeLayout.setVisibility(View.GONE);

            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<MovieListParser> nowPlaying = tmdbService.getNowPlaying();

            nowPlaying.enqueue(new Callback<MovieListParser>() {
                @Override
                public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                    pb.setVisibility(View.GONE);
                    if(response.code() != 200){
                        setComplete(400);
                    }else{
                        mNowPlayingList = response.body().getMovies();
                        populateNowPlaying(context);
                    }
                }

                @Override
                public void onFailure(Call<MovieListParser> call, Throwable t) {
                    setComplete(400);
                    pb.setVisibility(View.GONE);
                    mNowPlayingRelativeLayout.setVisibility(View.GONE);
                }
            });
        }else {
            setComplete(-1);
            pb.setVisibility(View.GONE);
        }
    }

    private void populateNowPlaying(final Context context){
        mNowPlayingRelativeLayout.setVisibility(View.VISIBLE);

        mNowPlayingRelativeLayout.post(new Runnable() {
            @Override
            public void run() {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mNowPlayingRelativeLayout.getWidth(), mNowPlayingRelativeLayout.getWidth() * 9 / 16);
                mNowPlayingRelativeLayout.setLayoutParams(params);
            }
        });

        mNowPlayingSliderLayout.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mNowPlayingSliderLayout.getWidth(), mNowPlayingSliderLayout.getWidth() * 9 / 16);
                mNowPlayingSliderLayout.setLayoutParams(params);

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
                mNowPlayingSliderLayout.setCurrentPosition(0, false);

                getGenres(MainActivity.this);
            }
        });
    }

    private void getGenres(final Context context){
        if(Utils.isInternetConnected(context)) {

            mGenreRecyclerView.setVisibility(View.VISIBLE);

            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<GenreParser> genre = tmdbService.getGenres();

            genre.enqueue(new Callback<GenreParser>() {
                @Override
                public void onResponse(Call<GenreParser> call, Response<GenreParser> response) {
                    if(response.code() != 200){
                        setComplete(400);
                    }else {
                        mGenreRecyclerView.setVisibility(View.VISIBLE);
                        mGenres = response.body().getGenres();
                        mGenreRecyclerView.setAdapter(new GenreAdapter(context, mGenres));

                        getPopular(MainActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<GenreParser> call, Throwable t) {
                    setComplete(400);
                }
            });
        }else {
            setComplete(-1);
        }
    }

    private void getPopular(final Context context){
        if(Utils.isInternetConnected(context)) {

            contentContainer.setVisibility(View.INVISIBLE);

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

            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<MovieListParser> popular = tmdbService.getPopular();

            popular.enqueue(new Callback<MovieListParser>() {
                @Override
                public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                    if(response.code() != 200){
                        setComplete(400);
                    }else {
                        contentContainer.setVisibility(View.VISIBLE);
                        mPopularList = response.body().getMovies();
                        mPopularList.add(new Movie(-1));

                        mPopularRecyclerView.setAdapter(new SummaryMovieAdapter(context, mPopularList, ListMovie.POPULAR));

                        getTopRated(MainActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<MovieListParser> call, Throwable t) {
                    setComplete(400);
                }
            });
        }else {
            setComplete(-1);
        }
    }

    private void getTopRated(final Context context){
        if(Utils.isInternetConnected(context)) {

            contentContainer.setVisibility(View.INVISIBLE);

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

            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<MovieListParser> topRated = tmdbService.getTopRated();

            topRated.enqueue(new Callback<MovieListParser>() {
                @Override
                public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                    if(response.code() != 200){
                        isSuccess = false;
                        setComplete(400);
                    }else {
                        findViewById(R.id.content_container).setVisibility(View.VISIBLE);
                        mTopRatedList = response.body().getMovies();
                        mTopRatedList.add(new Movie(-1));

                        mTopRatedRecyclerView.setAdapter(new SummaryMovieAdapter(context, mTopRatedList, ListMovie.TOP_RATED));

                        setComplete();
                    }
                }

                @Override
                public void onFailure(Call<MovieListParser> call, Throwable t) {
                    setComplete(400);
                }
            });
        }else {
            setComplete(-1);
        }
    }

    private void viewAll(String target){
        Intent i = new Intent(MainActivity.this, ListMovie.class);
        i.putExtra(target, true);
        startActivity(i);
    }

    private void setComplete(){
        if(!isSuccess){
            mNowPlayingRelativeLayout.setVisibility(View.GONE);
            contentContainer.setVisibility(View.GONE);
            mGenreRecyclerView.setVisibility(View.GONE);
        }
        Utils.setProcessComplete(mainProgressBar);
    }

    private void setComplete(int error){
        isSuccess = false;
        Utils.setProcessError(this, mNotificationTextView, error);
        findViewById(R.id.content_container).setVisibility(View.GONE);
        setComplete();
        fabListener.setFabShouldBeShown(true);
        refreshFab.show(fabListener);
        refreshFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSuccess = true;
                fabListener.setFabShouldBeShown(false);
                refreshFab.hide(fabListener);
                mNotificationTextView.setVisibility(View.GONE);
                mainProgressBar.setVisibility(View.VISIBLE);
                getNowPlaying(MainActivity.this);
            }
        });
    }
}
