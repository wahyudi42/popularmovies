package id.co.lazystudio.watchIt_freemoviedatabase;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbClient;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbService;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Genre;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.GenreResponse;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.MovieListResponse;
import id.co.lazystudio.watchIt_freemoviedatabase.sync.WatchItSyncAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.MySliderView;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<Movie> mNowPlayingList = new ArrayList<>();
    private List<Movie> mPopularList = new ArrayList<>();
    private List<Genre> mGenres = new ArrayList<>();
    private TmdbConfigurationPreference configurationPreference = new TmdbConfigurationPreference(this);

    SliderLayout mNowPlayingSliderLayout;
    RelativeLayout nowPlayingRelativeLayout;

    LinearLayout mGenreLinearLayout, mPopularLinearLayout;

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
            else
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

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

        mGenreLinearLayout = (LinearLayout) findViewById(R.id.genre_linearlayout);

        mPopularLinearLayout = (LinearLayout) findViewById(R.id.popular_linearlayout);
        mPopularLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                HorizontalScrollView parent = (HorizontalScrollView) mPopularLinearLayout.getParent();
                HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.WRAP_CONTENT, parent.getWidth() / 2);
                params.gravity = Gravity.CENTER;
                mPopularLinearLayout.setLayoutParams(params);

                findViewById(R.id.popular_viewall_textview).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("view all clicked", "popular");
                    }
                });
            }
        });

        getNowPlaying(this);
        getGenres(this);
        getPopular(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

            final Call<MovieListResponse> nowPlaying = tmdbService.getNowPlaying();

            nowPlaying.enqueue(new Callback<MovieListResponse>() {
                @Override
                public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                    pb.setVisibility(View.GONE);
                    mNowPlayingList = response.body().getMovies();
                    populateNowPlaying(context);
                }

                @Override
                public void onFailure(Call<MovieListResponse> call, Throwable t) {
                    pb.setVisibility(View.GONE);
                }
            });
        }else {
            pb.setVisibility(View.GONE);
        }
    }

    private void populateNowPlaying(Context context){
        for(int i = 0; i < mNowPlayingList.size(); i++){
            Movie nowPlaying = mNowPlayingList.get(i);
            String imagePath = nowPlaying.getBackdropPath(context, 0);
            MySliderView textSliderView = new MySliderView(context, i);
            textSliderView
                    .description(nowPlaying.getTitle())
                    .image(imagePath)
                    .setOnSliderClickListener(new MySliderView.OnSliderClickListener());

            mNowPlayingSliderLayout.addSlider(textSliderView);
        }
        MySliderView textSliderView = new MySliderView(context, 0);
        textSliderView
                .image(R.drawable.now_playing_view_more)
                .setOnSliderClickListener(new MySliderView.OnSliderClickListener());

        mNowPlayingSliderLayout.addSlider(textSliderView);

        mNowPlayingSliderLayout.setCustomIndicator((PagerIndicator) findViewById(R.id.custom_indicator));
    }

    private void getGenres(final Context context){
        final ProgressBar pb = (ProgressBar) findViewById(R.id.genre_progressbar);
        if(Utils.isInternetConnected(context)) {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<GenreResponse> genre = tmdbService.getGenres();

            genre.enqueue(new Callback<GenreResponse>() {
                @Override
                public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                    pb.setVisibility(View.GONE);
                    mGenreLinearLayout.setVisibility(View.VISIBLE);
                    HorizontalScrollView.LayoutParams params = (HorizontalScrollView.LayoutParams) mGenreLinearLayout.getLayoutParams();
                    params.gravity = Gravity.NO_GRAVITY;
                    mGenres = response.body().getGenres();
                    populateGenres(context);
                }

                @Override
                public void onFailure(Call<GenreResponse> call, Throwable t) {
                    pb.setVisibility(View.GONE);
                }
            });
        }else {
            pb.setVisibility(View.GONE);
        }
    }

    private void populateGenres(Context context){
        for(int j = 0; j < mGenres.size(); j++){
            Genre genre = mGenres.get(j);
            View v = getLayoutInflater().inflate(R.layout.item_genres, mGenreLinearLayout, false);
            v.setTag(j);
            TextView genreTextView = (TextView) v.findViewById(R.id.genre_text_view);
            genreTextView.setText(genre.getName());
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("clicked genre index", String.valueOf(view.getTag()));
                }
            });
            mGenreLinearLayout.addView(v);
        }
    }

    private void getPopular(final Context context){
        final ProgressBar pb = (ProgressBar) findViewById(R.id.popular_progressbar);
        if(Utils.isInternetConnected(context)) {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<MovieListResponse> genre = tmdbService.getPopular();

            genre.enqueue(new Callback<MovieListResponse>() {
                @Override
                public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                    pb.setVisibility(View.GONE);
                    mPopularLinearLayout.setVisibility(View.VISIBLE);
                    HorizontalScrollView.LayoutParams params = (HorizontalScrollView.LayoutParams) mPopularLinearLayout.getLayoutParams();
                    params.gravity = Gravity.NO_GRAVITY;
                    mPopularList = response.body().getMovies();
                    populatePopular(context);
                }

                @Override
                public void onFailure(Call<MovieListResponse> call, Throwable t) {
                    pb.setVisibility(View.GONE);
                }
            });
        }else {
            pb.setVisibility(View.GONE);
        }
    }

    private void populatePopular(Context context){
        for(int j = 0; j < mPopularList.size(); j++){
            Movie popular = mPopularList.get(j);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(
                    context.getResources().getDimensionPixelSize(R.dimen.small_margin),
                    context.getResources().getDimensionPixelSize(R.dimen.small_line_spacing),
                    context.getResources().getDimensionPixelSize(R.dimen.small_margin),
                    context.getResources().getDimensionPixelSize(R.dimen.small_line_spacing)
            );
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(params);
            imageView.setAdjustViewBounds(true);
            imageView.setTag(j);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("clicked popular index", String.valueOf(view.getTag()));
                }
            });

            mPopularLinearLayout.addView(imageView);

            Picasso.with(context).load(popular.getPosterPath(context, 0)).into(imageView);
        }
    }
}
