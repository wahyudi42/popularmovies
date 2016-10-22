package id.co.lazystudio.popularmovies;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.lazystudio.popularmovies.adapter.ListMovieAdapter;
import id.co.lazystudio.popularmovies.connection.TmdbClient;
import id.co.lazystudio.popularmovies.connection.TmdbService;
import id.co.lazystudio.popularmovies.entity.Movie;
import id.co.lazystudio.popularmovies.listener.EndlessRecyclerViewScrollListener;
import id.co.lazystudio.popularmovies.parser.MovieListParser;
import id.co.lazystudio.popularmovies.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";

    List<Movie> mMovieList = new ArrayList<>();
    private ProgressBar listProgressBar;
    private String mType;

    private int mPage = 1;
    private int mTotalPage = 0;
    private int mTotalItem = 0;
    private boolean loadingMore = false;

    private boolean isSuccess = true;

    TextView mNotificationTextView;

    ListMovieAdapter mListMovieAdapter;
    RecyclerView mListMovieRecyclerView;

    EndlessRecyclerViewScrollListener endlessListener;

    BroadcastReceiver mNetworkReceiver;

    IntentFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        toolbar.setTitleTextColor(Color.WHITE);

        listProgressBar = (ProgressBar) findViewById(R.id.list_movie_progressbar);
        mNotificationTextView = (TextView) findViewById(R.id.notification_textview);

        mType = POPULAR;
        getSupportActionBar().setTitle(getResources().getString(R.string.popular_title));

        mListMovieRecyclerView = (RecyclerView) findViewById(R.id.list_movie_recyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mListMovieRecyclerView.setLayoutManager(layoutManager);
        mListMovieAdapter = new ListMovieAdapter(this, mMovieList);
        mListMovieRecyclerView.setAdapter(mListMovieAdapter);

        endlessListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!(loadingMore) && mPage < mTotalPage) {
                    getMovieList(MainActivity.this);
                }
            }
        };

        mNetworkReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(Utils.isInternetConnected(context)){
                    getMovieList(MainActivity.this);

                    mListMovieRecyclerView.addOnScrollListener(endlessListener);
                }else{
                    mListMovieRecyclerView.removeOnScrollListener(endlessListener);
                    setComplete(-1);
                }
            }
        };

        mFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(mNetworkReceiver, mFilter);

        getMovieList(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.sort_popular:
                mType = POPULAR;
                setSortOrder();
                break;
            case R.id.sort_toprated:
                mType = TOP_RATED;
                setSortOrder();
                break;
        }
        return true;
    }

    private void setSortOrder(){
        if(mType.equals(TOP_RATED))
            getSupportActionBar().setTitle(getResources().getString(R.string.toprated_title));
        else
            getSupportActionBar().setTitle(getResources().getString(R.string.popular_title));
        mPage = 1;
        mMovieList.clear();
        mListMovieAdapter.updateData(0);
        getMovieList(this);
    }

    private void getMovieList(final Context context){
        if(!loadingMore) {
            if (Utils.isInternetConnected(context)) {

                Log.e("page", mPage + "");

                mNotificationTextView.setVisibility(View.GONE);
                listProgressBar.setVisibility(View.VISIBLE);

                loadingMore = true;

                TmdbService tmdbService =
                        TmdbClient.getClient().create(TmdbService.class);

                Call<MovieListParser> movieListCall;
                if (mType.equals(TOP_RATED)) {
                    movieListCall = tmdbService.getTopRated(mPage);
                } else {
                    movieListCall = tmdbService.getPopular(mPage);
                }

                movieListCall.enqueue(new Callback<MovieListParser>() {
                    @Override
                    public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                        if (response.code() != 200) {
                            setComplete(400);
                        } else {
                            isSuccess = true;
                            mTotalItem = response.body().getTotalResult();
                            mListMovieAdapter.setTotalItem(mTotalItem);
                            mMovieList.addAll(response.body().getMovies());
                            mTotalPage = response.body().getTotalPages();

                            if (mMovieList.size() > 0)
                                setComplete();
                            else
                                setComplete(200);
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieListParser> call, Throwable t) {
                        setComplete(400);
                    }
                });
            } else {
                setComplete(-1);
            }
        }
    }

    private void setComplete() {
        loadingMore = false;
        Utils.setProcessComplete(listProgressBar);
        if(isSuccess)
            ++mPage;
        mListMovieAdapter.updateData(mTotalItem);
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
