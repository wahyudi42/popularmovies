package id.co.lazystudio.watchIt_freemoviedatabase;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;

public class DetailMovie extends AppCompatActivity {
    public static final String MOVIE_KEY = "movie";
    private Movie mMovie;

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

        Log.e("movie", mMovie.getId()+" - "+mMovie.getTitle());

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

                RelativeLayout movieRelativeLayout = (RelativeLayout) findViewById(R.id.movie_title_relativelayout);
                LinearLayout.LayoutParams titleParams = (LinearLayout.LayoutParams) movieRelativeLayout.getLayoutParams();
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

        ((TextView) findViewById(R.id.movie_title_textview)).setText(mMovie.getTitle());
    }

}
