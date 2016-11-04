package id.co.lazystudio.popularmovies;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import id.co.lazystudio.popularmovies.entity.Movie;

public class DetailMovie extends AppCompatActivity {
    public static final String MOVIE_KEY = "movie";
    private Movie mMovie;

    ImageView backdropImageView;
    ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        Bundle args = getIntent().getExtras();
        mMovie = args.getParcelable(MOVIE_KEY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        RelativeLayout.LayoutParams toolbarParams = (RelativeLayout.LayoutParams) findViewById(R.id.stub_statusbar).getLayoutParams();
        toolbarParams.height = getStatusBarHeight();

        backdropImageView = (ImageView) findViewById(R.id.backdrop_imageview);

        FloatingActionButton fabFavorite = (FloatingActionButton)findViewById(R.id.favorite_fab);
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Favorite fab clicked", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        populateView();
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

    private void populateView(){
        /* BACKDROP */
        backdropImageView.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) backdropImageView.getLayoutParams();
                params.height = backdropImageView.getWidth() * 9 / 16;
            }
        });

        Picasso.with(this)
                .load(mMovie.getBackdropPath(this, 1))
                .error(R.drawable.no_image_land)
                .fit()
                .centerCrop()
                .into(backdropImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.backdrop_progressbar).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        findViewById(R.id.backdrop_progressbar).setVisibility(View.GONE);
                    }
                });

        /* POSTER */
        posterImageView = (ImageView) findViewById(R.id.poster_imageview);
        posterImageView.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) posterImageView.getLayoutParams();
                params.height = posterImageView.getWidth() * 3 / 2;

                LinearLayout container = (LinearLayout) findViewById(R.id.content_container);
                RelativeLayout.LayoutParams containerParams = (RelativeLayout.LayoutParams) container.getLayoutParams();
                containerParams.setMargins(0, -((params.height / 3)), 0, 0);

                FrameLayout movieFrameLayout = (FrameLayout) findViewById(R.id.movie_title_framelayout);
                LinearLayout.LayoutParams titleParams = (LinearLayout.LayoutParams) movieFrameLayout.getLayoutParams();
                titleParams.setMargins(0, (params.height / 3), 0, 0);

                View titleView = findViewById(R.id.movie_title_view);
                RelativeLayout.LayoutParams viewParams = (RelativeLayout.LayoutParams)titleView.getLayoutParams();
                viewParams.height = movieFrameLayout.getHeight()*2;
                viewParams.setMargins(0, (params.height / 3), 0, 0);
            }
        });

        Picasso.with(this)
                .load(mMovie.getPosterPath(this, 0))
                .error(R.drawable.no_image_port)
                .fit()
                .centerCrop()
                .into(posterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
                    }
                });

        TextView titleTextView = ((TextView) findViewById(R.id.movie_title_textview));
        titleTextView.setText(mMovie.getTitle());
        TextView releaseDateTextView = ((TextView) findViewById(R.id.movie_releasedate_textview));
        releaseDateTextView.setVisibility(View.VISIBLE);
        releaseDateTextView.setText(mMovie.getReleaseDate());
        TextView rateAvgTextView = ((TextView) findViewById(R.id.movie_rateaverage_textview));
        rateAvgTextView.setText(mMovie.getVoteAverage());
        TextView rateCountTextView = ((TextView) findViewById(R.id.movie_ratecount_textview));
        if(mMovie.getVoteCount() > 0){
            rateCountTextView.setVisibility(View.VISIBLE);
            rateCountTextView.setText(String.valueOf(mMovie.getVoteCount()));
        }

        ((TextView) findViewById(R.id.movie_popularity_textview)).setText(mMovie.getPopularity());

        findViewById(R.id.movie_overview_relativelayout).setVisibility(View.VISIBLE);
        TextView overviewTextView = (TextView) findViewById(R.id.movie_overview_textview);
        overviewTextView.setText(mMovie.getOverview());
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
