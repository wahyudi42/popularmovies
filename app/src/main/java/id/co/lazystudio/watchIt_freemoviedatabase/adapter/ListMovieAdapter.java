package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.DetailMovie;
import id.co.lazystudio.watchIt_freemoviedatabase.R;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;

/**
 * Created by faqiharifian on 27/09/16.
 */
public class ListMovieAdapter extends BaseAdapter {
    Context mContext;
    List<Movie> mMovieList;

    // Constructor
    public ListMovieAdapter(Context c, List<Movie> movies){
        mContext = c;
        mMovieList = movies;
    }

    @Override
    public int getCount() {
        return mMovieList.size();
    }

    @Override
    public Object getItem(int position) {
        return mMovieList.get(position);
    }

    @Override
    public long getItemId(int position) {
       return (long) mMovieList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v= inflater.inflate(R.layout.item_list_movie, parent, false);

        Movie movie = mMovieList.get(position);


        final ImageView imageView = (ImageView) v.findViewById(R.id.poster_imageview);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout posterRelativeLayout = (RelativeLayout) v.findViewById(R.id.poster_relativelayout);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, posterRelativeLayout.getWidth() * 3 / 2);
                imageView.setLayoutParams(params);
            }
        });

        Picasso.with(mContext)
                .load(movie.getPosterPath(mContext, 0))
                .error(R.drawable.no_image_port)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        v.findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        TextView title = (TextView) v.findViewById(R.id.title_textview);
        title.setText(movie.getTitle());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, DetailMovie.class);
                i.putExtra(DetailMovie.MOVIE_KEY, mMovieList.get(position));
                mContext.startActivity(i);
            }
        });

        return v;
    }
}
