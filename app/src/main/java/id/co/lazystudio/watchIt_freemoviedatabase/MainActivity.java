package id.co.lazystudio.watchIt_freemoviedatabase;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbClient;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbService;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.NowPlaying;
import id.co.lazystudio.watchIt_freemoviedatabase.sync.WatchItSyncAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.MySliderView;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<Movie> mNowPlayingList = new ArrayList<>();
    private TmdbConfigurationPreference configurationPreference = new TmdbConfigurationPreference(this);

    SliderLayout mNowPlayingSliderLayout;
    RelativeLayout nowPlayingRelativeLayout;

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
                ScrollView.LayoutParams params = new ScrollView.LayoutParams(nowPlayingRelativeLayout.getWidth(), nowPlayingRelativeLayout.getWidth() * 9 / 16);
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

        getNowPlaying(this);
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

            final Call<NowPlaying> nowPlaying = tmdbService.getNowPlaying();

            nowPlaying.enqueue(new Callback<NowPlaying>() {
                @Override
                public void onResponse(Call<NowPlaying> call, Response<NowPlaying> response) {
                    pb.setVisibility(View.GONE);
                    mNowPlayingList = response.body().getResults();
                    populateNowPlaying(context);
                }

                @Override
                public void onFailure(Call<NowPlaying> call, Throwable t) {
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
            StringBuilder imagePath = new StringBuilder();
            imagePath.append(configurationPreference.getBaseUrl());
            imagePath.append(configurationPreference.getBackdropSizes().get(0));
            imagePath.append(nowPlaying.getBackdropPath());
            MySliderView textSliderView = new MySliderView(context, i);
            textSliderView
                    .description(nowPlaying.getTitle())
                    .image(imagePath.toString())
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
}
