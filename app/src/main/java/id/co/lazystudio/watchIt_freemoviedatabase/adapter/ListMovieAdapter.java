package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v= inflater.inflate(R.layout.item_list_movie, parent, false);

        Movie movie = mMovieList.get(position);

        ImageView imageView = (ImageView) v.findViewById(R.id.poster_imageview);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.height = params.width * 3 / 2;
        Picasso.with(mContext).load(movie.getPosterPath(mContext, 0)).error(R.drawable.no_image_port).into(imageView);

        TextView title = (TextView) v.findViewById(R.id.title_textview);
        title.setText(movie.getTitle());

        return v;
    }
}
