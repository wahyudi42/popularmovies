package id.co.lazystudio.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.lazystudio.popularmovies.adapter.MyFavoriteAdapter;
import id.co.lazystudio.popularmovies.data.MovieContract;
import id.co.lazystudio.popularmovies.entity.Movie;
import id.co.lazystudio.popularmovies.listener.EndlessRecyclerViewScrollListener;
import id.co.lazystudio.popularmovies.utils.Utils;

import static android.os.Build.VERSION_CODES.M;

public class MyFavoriteActivity extends AppCompatActivity {

    public static final String LOG_TAG = MyFavoriteActivity.class.getSimpleName();

    List<Movie> mMovieList = new ArrayList<>();
    private ProgressBar listProgressBar;
    private String mType;

    private int mPage = 1;
    private int mTotalPage = 0;
    private int mTotalItem = 0;
    private boolean loadingMore = false;

    private boolean isSuccess = true;

    TextView mNotificationTextView;

    MyFavoriteAdapter mMyFavoriteAdapter;
    RecyclerView mListMovieRecyclerView;

    EndlessRecyclerViewScrollListener endlessListener;

    BroadcastReceiver mNetworkReceiver;

    IntentFilter mFilter;

    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_favorite);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= M)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listProgressBar = (ProgressBar) findViewById(R.id.list_movie_progressbar);
        mNotificationTextView = (TextView) findViewById(R.id.notification_textview);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_my_favorite));

        mListMovieRecyclerView = (RecyclerView) findViewById(R.id.list_movie_recyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        //mListMovieRecyclerView.setLayoutManager(layoutManager);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mListMovieRecyclerView.setLayoutManager(mLayoutManager);
        mListMovieRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mMyFavoriteAdapter = new MyFavoriteAdapter(this, mMovieList);
        mListMovieRecyclerView.setAdapter(mMyFavoriteAdapter);

        Log.v("Total Movie 1",""+mMovieList.size());

        getDataFromDb();

        Log.v("Total Movie 2",""+mMovieList.size());

        if (mMovieList.size() > 0){
            isSuccess = true;
            mTotalItem = mMovieList.size();
            setComplete();
        }
        else
            setComplete(200);

        mNetworkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(Utils.isInternetConnected(context)){
                    //show ads
                }else{
                    setComplete(-1);
                }
            }
        };

        mFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(mNetworkReceiver, mFilter);

    }
    private void getDataFromDb(){
        Uri movieUri = MovieContract.MovieEntry.buildMovie();
        Log.v(LOG_TAG, "URI: " + movieUri);

        String sortOrder = MovieContract.MovieEntry._ID + " DESC";

        Cursor cursor;
        int nCursor;

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        cursor = getContentResolver().query(
                movieUri,
                null,
                null,
                null,
                sortOrder
        );

        if(cursor == null){
            Toast.makeText(getBaseContext(),"Database kosong", Toast.LENGTH_SHORT).show();
        }else {
            cursor.moveToFirst();
            Log.v("Cursor Object", DatabaseUtils.dumpCursorToString(cursor));
            boolean isCursor = cursor.moveToFirst();
            nCursor = cursor.getCount();
            Log.v(LOG_TAG,"Jml cursor:"+ nCursor);
            if(isCursor){
                for (int i=0;i<nCursor;i++){
                    Movie movie = new Movie();
                    int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ID));
                    String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE));
                    String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW));
                    String releaseDate = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE));
                    String posterPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH));
                    String backdropPath = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH));
                    int voteCount = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT));
                    float popularity = (float) cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POPULARITY));
                    float voteAverage = (float) cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE));

                    movie.setId(id);
                    movie.setTitle(title);
                    movie.setOverview(overview);
                    movie.setReleaseDate(releaseDate);
                    movie.setPosterPath(posterPath);
                    movie.setBackdropPath(backdropPath);
                    movie.setVoteCount(voteCount);
                    movie.setPopularity(popularity);
                    movie.setVoteAverage(voteAverage);

                    Log.v(LOG_TAG,"judul "+ cursor.getPosition()+": "+posterPath);
                    mMovieList.add(movie);

                    cursor.moveToNext();
                }

            }
        }
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

    private void setComplete() {
        loadingMore = false;
        Utils.setProcessComplete(listProgressBar);
        if(isSuccess)
            ++mPage;
        mMyFavoriteAdapter.updateData(mTotalItem);
    }

    private void setComplete(int error){
        loadingMore = false;
        isSuccess = false;
        Utils.setProcessError(this, mNotificationTextView, error);
        setComplete();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(mNetworkReceiver);
        super.onStop();
    }

    @Override
    protected void onResume() {
        registerReceiver(mNetworkReceiver, mFilter);
        super.onResume();
    }
}
