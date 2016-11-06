package id.co.lazystudio.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import id.co.lazystudio.popularmovies.data.MovieContract;
import id.co.lazystudio.popularmovies.data.MovieContract.MovieEntry;
import id.co.lazystudio.popularmovies.entity.Movie;

import static id.co.lazystudio.popularmovies.MyFavoriteActivity.LOG_TAG;

public class DetailMovie extends AppCompatActivity {
    public static final String MOVIE_KEY = "movie";
    private Movie mMovie;

    ImageView backdropImageView;
    ImageView posterImageView;

    FloatingActionButton fabFavorite;
    Uri currentUri;

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

        fabFavorite = (FloatingActionButton)findViewById(R.id.favorite_fab);

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

        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean success = saveMovie(mMovie.getId(),
                        mMovie.getTitle(),
                        mMovie.getOverview(),
                        mMovie.getReleaseDateDb(),
                        mMovie.getPosterPathDb(),
                        mMovie.getBackdropPathDb(),
                        mMovie.getVoteCount(),
                        mMovie.getPopularityDb(),
                        mMovie.getVoteAverageDb());
                if(success){
                    Snackbar.make(view, "Movie "+ mMovie.getTitle() + " added to favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                getMovieStatus(mMovie.getId());
            }
        });

        getMovieStatus(mMovie.getId());
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private boolean saveMovie(int id, String title, String overview, String releaseDate, String posterPath,
                              String backdropPath, int voteCount, String popularity, String voteAverage){
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieEntry.COLUMN_ID, id);
        movieValues.put(MovieEntry.COLUMN_TITLE, title);
        movieValues.put(MovieEntry.COLUMN_OVERVIEW, overview);
        movieValues.put(MovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        movieValues.put(MovieEntry.COLUMN_POSTER_PATH, posterPath);
        movieValues.put(MovieEntry.COLUMN_BACKDROP_PATH, backdropPath);
        movieValues.put(MovieEntry.COLUMN_VOTE_COUNT, voteCount);
        movieValues.put(MovieEntry.COLUMN_POPULARITY, popularity);
        movieValues.put(MovieEntry.COLUMN_VOTE_AVERAGE, voteAverage);


        if(currentUri==null){
            getContentResolver().insert(MovieEntry.CONTENT_URI,movieValues);
            return true;
        }
        return true;
    }

    private void delete(int id) {
        getContentResolver().delete(MovieEntry.CONTENT_URI, MovieEntry.COLUMN_ID+" =?", new String[]{String.valueOf(id)});
    }

    private void getMovieStatus(int id){
        Uri movieUri = MovieContract.MovieEntry.buildMovie();
        Log.v(LOG_TAG, "URI: " + movieUri);

        String sortOrder = MovieContract.MovieEntry._ID + " ASC";
        String selectionClause = "id_movie = " + id;

        Cursor cursor;
        int nCursor;

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        cursor = getContentResolver().query(
                movieUri,
                null,
                selectionClause,
                null,
                sortOrder
        );

        if(cursor == null){
            Toast.makeText(getBaseContext(),"Database kosong", Toast.LENGTH_SHORT).show();
        }else {
            cursor.moveToFirst();
            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
            nCursor = cursor.getCount();
            Log.v(LOG_TAG,"Jml cursor:"+ nCursor);
            if(nCursor>0){
                fabFavorite.setImageResource(R.drawable.ic_star_black_24dp);
                fabFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete(mMovie.getId());
                        Snackbar.make(view, "Movie "+ mMovie.getTitle() + " removed from favorite", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        finish();
                    }
                });
            }
        }
    }

}
