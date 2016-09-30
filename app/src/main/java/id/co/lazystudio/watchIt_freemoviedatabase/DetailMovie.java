package id.co.lazystudio.watchIt_freemoviedatabase;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbClient;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbService;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Collection;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Company;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Genre;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Keyword;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Video;
import id.co.lazystudio.watchIt_freemoviedatabase.parser.MovieParser;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.Utils;
import retrofit2.Call;
import retrofit2.Response;

public class DetailMovie extends AppCompatActivity {
    public static final String MOVIE_KEY = "movie";
    private Movie mMovie;
    private Collection mCollection = null;
    private List<Company> mCompanyList = new ArrayList<>();
    private List<Genre> mGenreList = new ArrayList<>();
    private List<Image> mBackdropList = new ArrayList<>();
    private List<Image> mPosterList = new ArrayList<>();
    private List<Keyword> mKeywordList = new ArrayList<>();
    private List<Video> mVideo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
            else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        Bundle args = getIntent().getExtras();
        mMovie = args.getParcelable(MOVIE_KEY);

        getMovie();
        populateView();
    }

    private void getMovie(){
        final ProgressBar pb = (ProgressBar) findViewById(R.id.detail_movie_progressbar);
        if(Utils.isInternetConnected(this)) {
            TmdbService tmdbService =
                    TmdbClient.getClient().create(TmdbService.class);

            final Call<MovieParser> movie = tmdbService.getMovie(mMovie.getId());

            movie.enqueue(new retrofit2.Callback<MovieParser>() {
                @Override
                public void onResponse(Call<MovieParser> call, Response<MovieParser> response) {
                    Log.e("complete", "true");
                    MovieParser movieParser = response.body();
                    mMovie = (Movie)response.body();
                    mCollection = movieParser.getCollection();
                    mCompanyList = movieParser.getCompanies();
                    mGenreList = movieParser.getGenres();
                    mBackdropList = movieParser.getBackdrops();
                    mPosterList = movieParser.getPosters();
                    mKeywordList = movieParser.getKeywords();
                    mVideo = movieParser.getVideos();
//                    pb.setVisibility(View.GONE);
                    populateView();
                    Log.e("overview", mMovie.getOverview());
                }

                @Override
                public void onFailure(Call<MovieParser> call, Throwable t) {
                    t.printStackTrace();
//                    pb.setVisibility(View.GONE);
                }
            });
        }else {
//            pb.setVisibility(View.GONE);
        }
    }

    private void populateView(){
        final ImageView backdropImageView = (ImageView) findViewById(R.id.backdrop_imageview);
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
                .into(backdropImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.backdrop_progressbar).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        final ImageView posterImageView = (ImageView) findViewById(R.id.poster_imageview);
        posterImageView.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) posterImageView.getLayoutParams();
                params.height = posterImageView.getWidth() * 3 / 2;

                LinearLayout container = (LinearLayout) findViewById(R.id.content_container);
                RelativeLayout.LayoutParams containerParams = (RelativeLayout.LayoutParams) container.getLayoutParams();
                containerParams.setMargins(0, -(params.height / 3), 0, 0);

                FrameLayout movieFrameLayout = (FrameLayout) findViewById(R.id.movie_title_framelayout);
                LinearLayout.LayoutParams titleParams = (LinearLayout.LayoutParams) movieFrameLayout.getLayoutParams();
                titleParams.setMargins(0, params.height / 3, 0, 0);
            }
        });

        Picasso.with(this)
                .load(mMovie.getPosterPath(this, 0))
                .error(R.drawable.no_image_port)
                .into(posterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        TextView titleTextView = ((TextView) findViewById(R.id.movie_title_textview));
        titleTextView.setText(mMovie.getTitle());
        ((TextView) findViewById(R.id.movie_releasedate_textview)).setText(mMovie.getReleaseDate());
        ((TextView) findViewById(R.id.movie_runtime_textview)).setText(mMovie.getRuntime());
        TextView rateAvgTextView = ((TextView) findViewById(R.id.movie_rateaverage_textview));
        rateAvgTextView.setText(mMovie.getVoteAverage());
        TextView rateCountTextView = ((TextView) findViewById(R.id.movie_ratecount_textview));
        if(mMovie.getVoteCount() > 0){
            rateCountTextView.setVisibility(View.VISIBLE);
            rateCountTextView.setText(String.valueOf(mMovie.getVoteCount()));
        }

        ((TextView) findViewById(R.id.movie_popularity_textview)).setText(mMovie.getPopularity());

        if(mMovie.getTagline() != null) {
            if(!mMovie.getTagline().equals("")) {
                final RelativeLayout taglineRelativeLayout = (RelativeLayout) findViewById(R.id.movie_tagline_relativelayout);
                taglineRelativeLayout.setVisibility(View.VISIBLE);
                taglineRelativeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        final TextView taglineTextView = ((TextView) findViewById(R.id.movie_tagline_textview));
                        taglineTextView.setText(mMovie.getTagline());
                        taglineTextView.setMaxWidth(taglineRelativeLayout.getWidth() - (2 * getResources().getDimensionPixelSize(R.dimen.tag_width)));
                    }
                });

            }
        }


        if(mMovie.getOverview() != null){
            if(!mMovie.getOverview().equals("")){
                TextView overviewTextView = (TextView) findViewById(R.id.movie_overview_textview);
                overviewTextView.setVisibility(View.VISIBLE);
                overviewTextView.setText(mMovie.getOverview());
            }
        }

        if(mGenreList.size() > 0){
//            TagGroup genreTagGroup = (TagGroup) findViewById(R.id.genre_taggroup);
            TagContainerLayout genreContainer = (TagContainerLayout) findViewById(R.id.genre_tagcontainer);
//            GridLayout genreGridLayout = (GridLayout) findViewById(R.id.movie_genre_gridlayout);
//            genreGridLayout.setVisibility(View.VISIBLE);
//            genreTagGroup.setVisibility(View.VISIBLE);
            genreContainer.setVisibility(View.VISIBLE);
            String[] tags = new String[mGenreList.size()];
            for(int i = 0; i < mGenreList.size(); i++){
//                View v = getLayoutInflater().inflate(R.layout.item_genre_detail, genreGridLayout, false);
//                TextView genreTextView = (TextView) v.findViewById(R.id.genre_text_view);
//                genreTextView.setText(genre.getName());
//                v.setTag(genre.getId());
//                v.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Integer index = (Integer)view.getTag();
//                        Genre genre = mGenreList.get(index);
//                        Log.e("genre clicked", genre.getId()+" - "+genre);
//                    }
//                });
//                genreGridLayout.addView(v);
                tags[i] = mGenreList.get(i).getName();
            }
//            genreTagGroup.setTags(tags);
//            genreTagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
//                @Override
//                public void onTagClick(String tag) {
//                    Genre genre = null;
//                    for (Genre g : mGenreList){
//                        if(g.getName().equals(tag)){
//                            genre = g;
//                            break;
//                        }
//                    }
////                    Integer index = (Integer)view.getTag();
////                        Genre genre = mGenreList.get(index);
//                    if(genre != null)
//                        Log.e("genre clicked", genre.getId()+" - "+genre.getName());
//                }
//            });
            genreContainer.setTags(tags);
            genreContainer.setOnTagClickListener(new TagView.OnTagClickListener() {
                @Override
                public void onTagClick(int position, String text) {
                    Genre genre = mGenreList.get(position);
                    Log.e("genre clicked", genre.getId()+" - "+genre.getName());
                }

                @Override
                public void onTagLongClick(int position, String text) {

                }
            });
        }
    }
}
