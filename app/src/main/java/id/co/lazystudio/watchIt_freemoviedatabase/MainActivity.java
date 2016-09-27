package id.co.lazystudio.watchIt_freemoviedatabase;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
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

    LinearLayout mGenreLinearLayout, mPopularLinearLayout, mTopRatedLinearLayout;

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
                        viewAll(ListMovie.POPULAR);
                    }
                });
            }
        });

        mTopRatedLinearLayout = (LinearLayout) findViewById(R.id.toprated_linearlayout);
        mTopRatedLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                HorizontalScrollView parent = (HorizontalScrollView) mTopRatedLinearLayout.getParent();
                HorizontalScrollView.LayoutParams params = new HorizontalScrollView.LayoutParams(HorizontalScrollView.LayoutParams.WRAP_CONTENT, parent.getWidth() / 2);
                params.gravity = Gravity.CENTER;
                mTopRatedLinearLayout.setLayoutParams(params);

                findViewById(R.id.toprated_viewall_textview).setOnClickListener(new View.OnClickListener() {
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
        for(int i = 0; i <= mNowPlayingList.size(); i++){
            Movie nowPlaying = null;
            MySliderView textSliderView = null;
            if(i < mNowPlayingList.size()) {
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
                    Log.e("clicked now playing id", String.valueOf(index));
                    if(index == -1){
                        viewAll(ListMovie.NOW_PLAYING);
                    }
                }
            });

            mNowPlayingSliderLayout.addSlider(textSliderView);
        }

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

            final Call<MovieListResponse> popular = tmdbService.getPopular();

            popular.enqueue(new Callback<MovieListResponse>() {
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
        for(int j = 0; j <= mPopularList.size(); j++){
            Movie popular = null;
            if(j != mPopularList.size())
                popular = mPopularList.get(j);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(
                    context.getResources().getDimensionPixelSize(R.dimen.small_margin),
                    context.getResources().getDimensionPixelSize(R.dimen.small_line_spacing),
                    context.getResources().getDimensionPixelSize(R.dimen.small_margin),
                    context.getResources().getDimensionPixelSize(R.dimen.small_line_spacing)
            );
            final ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(params);
            imageView.setAdjustViewBounds(true);
            imageView.setTag(j < mPopularList.size() ? j : -1);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN: {
                            imageView.getDrawable().setColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP);
                            imageView.invalidate();
                            Log.e("clicked popular index", String.valueOf(view.getTag()));
                            if((Integer)view.getTag() == -1)
                                viewAll(ListMovie.POPULAR);
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            imageView.getDrawable().clearColorFilter();
                            imageView.invalidate();
                            break;
                        }
                    }
                    return true;
                }
            });

            mPopularLinearLayout.addView(imageView);

            if(popular != null)
                Picasso.with(context).load(popular.getPosterPath(context, 0)).error(R.drawable.no_image_port).into(imageView);
            else
                Picasso.with(context).load(R.drawable.more_port).error(R.drawable.no_image_port).into(imageView);
        }
    }

    private void getTopRated(final Context context){
        final ProgressBar pb = (ProgressBar) findViewById(R.id.toprated_progressbar);
        if(Utils.isInternetConnected(context)) {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<MovieListResponse> topRated = tmdbService.getTopRated();

            topRated.enqueue(new Callback<MovieListResponse>() {
                @Override
                public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                    pb.setVisibility(View.GONE);
                    mTopRatedLinearLayout.setVisibility(View.VISIBLE);
                    HorizontalScrollView.LayoutParams params = (HorizontalScrollView.LayoutParams) mTopRatedLinearLayout.getLayoutParams();
                    params.gravity = Gravity.NO_GRAVITY;
                    mTopRatedList = response.body().getMovies();
                    populateTopRated(context);
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

    private void populateTopRated(Context context){
        for(int k = 0; k <= mTopRatedList.size(); k++){
            Movie topRated = null;
            if(k < mTopRatedList.size())
                topRated = mTopRatedList.get(k);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.setMargins(
                    context.getResources().getDimensionPixelSize(R.dimen.small_margin),
                    context.getResources().getDimensionPixelSize(R.dimen.small_line_spacing),
                    context.getResources().getDimensionPixelSize(R.dimen.small_margin),
                    context.getResources().getDimensionPixelSize(R.dimen.small_line_spacing)
            );
            final ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(params);
            imageView.setAdjustViewBounds(true);
            imageView.setTag(k < mPopularList.size() ? k : -1);
            imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN: {
                            imageView.getDrawable().setColorFilter(0x77000000,PorterDuff.Mode.SRC_ATOP);
                            imageView.invalidate();
                            Log.e("clicked top rated index", String.valueOf(view.getTag()));
                            if((Integer)view.getTag() == -1)
                                viewAll(ListMovie.TOP_RATED);
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            imageView.getDrawable().clearColorFilter();
                            imageView.invalidate();
                            break;
                        }
                    }
                    return true;
                }
            });

            mTopRatedLinearLayout.addView(imageView);

            if(topRated != null)
                Picasso.with(context).load(topRated.getPosterPath(context, 0)).error(R.drawable.no_image_port).into(imageView);
            else
                Picasso.with(context).load(R.drawable.more_port).error(R.drawable.no_image_port).into(imageView);
        }
    }

    private void viewAll(String target){
        Intent i = new Intent(MainActivity.this, ListMovie.class);
        i.putExtra(target, true);
        startActivity(i);
    }
}
