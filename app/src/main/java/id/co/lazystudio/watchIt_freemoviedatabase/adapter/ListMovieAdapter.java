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

import java.util.ArrayList;
import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.DetailMovie;
import id.co.lazystudio.watchIt_freemoviedatabase.R;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;

/**
 * Created by faqiharifian on 27/09/16.
 */
public class ListMovieAdapter extends BaseAdapter {
    Context mContext;
    List<Movie> mAllMovieList, mMovieList;
    int totalItem = 0;

    int currentPage = 0;

    // Constructor
    public ListMovieAdapter(Context c, List<Movie> movies){
        mContext = c;
        mAllMovieList = movies;
        mMovieList = new ArrayList<>();

        int maxItemIndex = ((int)(Math.floor(mAllMovieList.size() / 3))) * 3;

        for(int i = 0; i < maxItemIndex; i++){
            mMovieList.add(mAllMovieList.get(i));
        }
    }

    public void setCollection(){
        mMovieList = mAllMovieList;
    }

    public void setTotalItem(int totalItem){
        this.totalItem = totalItem;
        mMovieList = new ArrayList<>();

        int maxItemIndex;
        if(mMovieList.size() < totalItem){
            maxItemIndex = ((int)(Math.floor(mAllMovieList.size() / 3))) * 3;
        }else{
            maxItemIndex = totalItem;
        }

        for(int i = 0; i < maxItemIndex; i++){
            mMovieList.add(mAllMovieList.get(i));
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
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
        final Movie movie = mMovieList.get(position);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.item_list_movie, parent, false);

        final ImageView imageView = (ImageView) v.findViewById(R.id.poster_imageview);

        imageView.post(new Runnable() {
            @Override
            public void run() {
                final RelativeLayout posterRelativeLayout = (RelativeLayout) v.findViewById(R.id.poster_relativelayout);
                final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, posterRelativeLayout.getWidth() * 3 / 2);
                imageView.setLayoutParams(params);
            }
        });

                Picasso.with(mContext)
                        .load(movie.getPosterPath(mContext, 0))
                        .error(R.drawable.no_image_port)
                        .fit()
                        .centerCrop()
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                v.findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                v.findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
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

    public class ViewHolder{
        private View view;
        public ViewHolder(View view){

        }
    }
}
