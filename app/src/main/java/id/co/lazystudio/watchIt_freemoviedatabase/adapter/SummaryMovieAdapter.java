package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.DetailMovie;
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
    int id = -1;
    String title;

    public SummaryMovieAdapter(Context context, List<Movie> movies, String type){
        mContext = context;
        mMovies = movies;
        mMovies.add(new Movie(-1));
        mType = type;
    }

    public SummaryMovieAdapter(Context context, List<Movie> movies, String type, Movie movie){
        mContext = context;
        mMovies = movies;
        mMovies.add(new Movie(-1));
        mType = type;
        this.id = movie.getId();
        this.title = movie.getTitle();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int verticalMargin = mContext.getResources().getDimensionPixelSize(R.dimen.small_line_spacing);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                (parent.getHeight() - 2 * verticalMargin) * 2 / 3,
                parent.getHeight() - 2 * verticalMargin);
        params.setMargins(
                mContext.getResources().getDimensionPixelSize(R.dimen.smaller_margin),
                verticalMargin,
                mContext.getResources().getDimensionPixelSize(R.dimen.smaller_margin),
                verticalMargin
        );

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);
        v.setLayoutParams(params);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movie movie = mMovies.get(position);
        holder.bind(mContext, movie, mType, id);
    }

    @Override
    public int getItemCount() {
        return mMovies.size()-1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView posterImageView;
        ProgressBar progressBar;
        View parentView;

        public ViewHolder(View view){
            super(view);
            parentView = view;
            posterImageView = (ImageView) view.findViewById(R.id.poster_imageview);
            progressBar = (ProgressBar) view.findViewById(R.id.poster_progressbar);
        }

        public void bind(final Context context, final Movie movie, final String type, final int id){
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) parentView.getLayoutParams();
            if(movie.getId() == -1){
                Picasso.with(context)
                        .load(R.drawable.more_port)
                        .error(R.drawable.no_image_port)
                        .fit()
//                        .resize(params.width, params.height)
                        .centerCrop()
                        .into(posterImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }else{
                Picasso.with(context)
                        .load(movie.getPosterPath(context, 0))
                        .error(R.drawable.no_image_port)
                        .fit()
//                        .resize(params.width, params.height)
                        .centerCrop()
                        .into(posterImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }

            posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (movie.getId() == -1){
                        Intent i = new Intent(context, ListMovie.class);
                        i.putExtra(type, true);
                        if(id != -1) {
                            i.putExtra(ListMovie.KEY_ID, id);
                            i.putExtra(ListMovie.KEY_TITLE, title);
                        }
                        context.startActivity(i);
                    }else {
                        Log.e("clicked", movie.getId() + " - " + movie.getTitle());
                        Intent i = new Intent(context, DetailMovie.class);
                        i.putExtra(DetailMovie.MOVIE_KEY, movie);
                        context.startActivity(i);
                    }
                }
            });
        }
    }
}
