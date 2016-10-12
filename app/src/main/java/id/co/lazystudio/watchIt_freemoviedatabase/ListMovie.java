package id.co.lazystudio.watchIt_freemoviedatabase;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.adapter.ListMovieAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbClient;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbService;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;
import id.co.lazystudio.watchIt_freemoviedatabase.listener.EndlessRecyclerViewScrollListener;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.MovieListParser;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.FabVisibilityChangeListener;
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
    private ProgressBar listProgressBar;
    private String mType;
    private int mId = 0;

    private int mPage = 1;
    private int mTotalPage = 0;
    private int mTotalItem = 0;
    private boolean loadingMore = true;

    private boolean isSuccess = true;

    TextView mNotificationTextView;

    ListMovieAdapter mListMovieAdapter;
    RecyclerView mListMovieRecyclerView;

    FloatingActionButton refreshFab;
    FabVisibilityChangeListener fabListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, getTheme()));
        else
            toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        listProgressBar = (ProgressBar) findViewById(R.id.list_movie_progressbar);
        mNotificationTextView = (TextView) findViewById(R.id.notification_textview);

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

        mListMovieRecyclerView = (RecyclerView) findViewById(R.id.list_movie_recyclerview);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mListMovieRecyclerView.setLayoutManager(layoutManager);
        mListMovieAdapter = new ListMovieAdapter(this, mMovieList, mType);
        mListMovieRecyclerView.setAdapter(mListMovieAdapter);
        mListMovieRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!(loadingMore) && mPage < mTotalPage) {
                    getMovieList(ListMovie.this);
                }
            }
        });

        refreshFab = (FloatingActionButton) findViewById(R.id.refresh_fab);
        fabListener = new FabVisibilityChangeListener();

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
            Utils.initializeAd(this, findViewById(R.id.list_container));

            loadingMore = true;

            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            Call<MovieListParser> movieListCall;
            switch (mType) {
                case POPULAR:
                    movieListCall = tmdbService.getPopular(mPage);
                    break;
                case TOP_RATED:
                    movieListCall = tmdbService.getTopRated(mPage);
                    break;
                case GENRE:
                    movieListCall = tmdbService.getMoviesGenre(mId, mPage);
                    break;
                case COLLECTION:
                    movieListCall = tmdbService.getMoviesCollection(mId);
                    mPage = 9999;
                    break;
                case KEYWORD:
                    movieListCall = tmdbService.getMoviesKeyword(mId, mPage);
                    break;
                case SIMILAR:
                    movieListCall = tmdbService.getMoviesSimilar(mId, mPage);
                    break;
                default:
                    movieListCall = tmdbService.getNowPlaying(mPage);
                    break;
            }

            movieListCall.enqueue(new Callback<MovieListParser>() {
                @Override
                public void onResponse(Call<MovieListParser> call, Response<MovieListParser> response) {
                    if (response.code() != 200) {
                        setComplete(400);
                    } else {
                        isSuccess = true;
                        if(mType.equals(COLLECTION)){
                            mMovieList.addAll(response.body().getParts());
                        }else {
                            mTotalItem = response.body().getTotalResult();
                            mListMovieAdapter.setTotalItem(mTotalItem);
                            mMovieList.addAll(response.body().getMovies());
                            mTotalPage = response.body().getTotalPages();
                        }
                        if(mMovieList.size() > 0)
                            setComplete();
                        else
                            setComplete(200);
                        if(mPage <= 2 && mPage < mTotalPage){
                            getMovieList(context);
                        }
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

    private void setComplete() {
        loadingMore = false;
        Utils.setProcessComplete(listProgressBar);
        if(isSuccess)
            ++mPage;
        mListMovieAdapter.updateData(mTotalItem);
    }

    private void setComplete(int error){
        isSuccess = false;
        if(mPage > 1) {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        }else{
            Utils.setProcessError(this, mNotificationTextView, error);
            setComplete();
            if(error != 200) {
                fabListener.setFabShouldBeShown(true);
                refreshFab.show(fabListener);
                refreshFab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        fabListener.setFabShouldBeShown(false);
                        refreshFab.hide(fabListener);
                        mNotificationTextView.setVisibility(View.GONE);
                        listProgressBar.setVisibility(View.VISIBLE);
                        getMovieList(ListMovie.this);
                    }
                });
            }
        }
    }
}
