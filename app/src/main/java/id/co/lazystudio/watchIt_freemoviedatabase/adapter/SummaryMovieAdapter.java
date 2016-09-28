package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.ListMovie;
import id.co.lazystudio.watchIt_freemoviedatabase.R;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Movie;

/**
 * Created by faqiharifian on 28/09/16.
 */
public class SummaryMovieAdapter extends RecyclerView.Adapter<SummaryMovieAdapter.ViewHolder> {
    List<Movie> mMovies;
    Context mContext;
    String mType;

    public SummaryMovieAdapter(Context context, List<Movie> movies, String type){
        mContext = context;
        mMovies = movies;
        mMovies.add(new Movie(-1));
        mType = type;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.MATCH_PARENT);
        params.setMargins(
                mContext.getResources().getDimensionPixelSize(R.dimen.smaller_margin),
                mContext.getResources().getDimensionPixelSize(R.dimen.small_line_spacing),
                mContext.getResources().getDimensionPixelSize(R.dimen.smaller_margin),
                mContext.getResources().getDimensionPixelSize(R.dimen.small_line_spacing)
        );
        final ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(params);
        imageView.setAdjustViewBounds(true);
        imageView.setBackgroundResource(R.color.colorPrimaryDark);
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.bind(mContext, movie, mType);
    }

    @Override
    public int getItemCount() {
        return mMovies.size()-1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView posterImageView;

        public ViewHolder(View view){
            super(view);
            posterImageView = (ImageView) view;
        }

        public void bind(final Context context, final Movie movie, final String type){
            if(movie.getId() == -1){
                Picasso.with(context).load(R.drawable.more_port).error(R.drawable.no_image_port).into(posterImageView);
            }else{
                Picasso.with(context).load(movie.getPosterPath(context, 0)).error(R.drawable.no_image_port).into(posterImageView);
            }

            posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (movie.getId() == -1){
                        Intent i = new Intent(context, ListMovie.class);
                        i.putExtra(type, true);
                        context.startActivity(i);
                    }else {
                        Log.e("clicked", movie.getId() + " - " + movie.getTitle());
                    }
                }
            });
        }
    }
}
