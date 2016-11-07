package id.co.lazystudio.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import id.co.lazystudio.popularmovies.DetailMovie;
import id.co.lazystudio.popularmovies.R;
import id.co.lazystudio.popularmovies.entity.Movie;

import static id.co.lazystudio.popularmovies.R.id.adView;

/**
 * Created by faqiharifian on 04/10/16.
 */

public class MyFavoriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<Movie> mAllMovieList;
    List<Movie> mMovieList = new ArrayList<>();
    int totalItem = 0;

    int currentPage = 0;

    public static final int ITEM_TYPE_NORMAL= 0;
    public static final int ITEM_TYPE_WITH_ADS = 1;

    public MyFavoriteAdapter(Context c, List<Movie> movies){
        mContext = c;
        mAllMovieList = movies;
    }

    public void setTotalItem(int totalItem){
        this.totalItem = totalItem;
    }

    @Override
    public int getItemViewType(int position) {
        Log.v("Judul Item",""+mAllMovieList.get(position).getTitle());
        Log.v("Item ads",""+position+" ="+mAllMovieList.get(position).isAds());
        return (mAllMovieList.get(position).isAds() ? ITEM_TYPE_WITH_ADS : ITEM_TYPE_NORMAL);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType  == ITEM_TYPE_WITH_ADS) {
            final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ads, parent, false);
            return new CustomViewHolder(v); // view holder for header items
        }else if(viewType == ITEM_TYPE_NORMAL){
            final View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_favorite, parent, false);
            final ImageView imageView = (ImageView) v.findViewById(R.id.poster_imageview);
            final TextView title = (TextView) v.findViewById(R.id.title_textview);

            imageView.post(new Runnable() {
                @Override
                public void run() {
                    final RelativeLayout posterRelativeLayout = (RelativeLayout) v.findViewById(R.id.poster_relativelayout);
                    final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, posterRelativeLayout.getWidth() * 3 / 2);
                    imageView.setLayoutParams(params);
                }
            });
            return new ViewHolder(v); // view holder for normal items
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final int itemType = getItemViewType(position);
        if (itemType == ITEM_TYPE_WITH_ADS) {
            ((CustomViewHolder)holder).bind();
        }else if(itemType == ITEM_TYPE_NORMAL){
            Movie movie = mMovieList.get(position);
            ((ViewHolder)holder).bind(movie);
        }

    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private ImageView posterImageView;
        private TextView titleTextView, ratingTextView;
        private ProgressBar progressBar;
        public ViewHolder(View view){
            super(view);
            parentView = view;
            posterImageView = (ImageView) view.findViewById(R.id.poster_imageview);
            titleTextView = (TextView) view.findViewById(R.id.title_textview);
            ratingTextView = (TextView) view.findViewById(R.id.movie_vote_textview);
            progressBar = (ProgressBar) view.findViewById(R.id.poster_progressbar);
        }

        public void bind(final Movie movie){
            Picasso.with(mContext)
                    .load(movie.getPosterPath(mContext, 0))
                    .error(R.drawable.no_image_port)
                    .fit()
                    .centerCrop()
                    .into(posterImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            parentView.findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            parentView.findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
                        }
                    });

            titleTextView.setText(movie.getTitle());
            ratingTextView.setText(movie.getVoteAverage());

            ((ViewGroup)parentView).getChildAt(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, DetailMovie.class);
                    i.putExtra(DetailMovie.MOVIE_KEY, movie);
                    mContext.startActivity(i);
                }
            });
        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        private View parentView;
        private NativeExpressAdView nativeExpressAdView;

        public CustomViewHolder(View view){
            super(view);
            parentView = view;
            nativeExpressAdView = (NativeExpressAdView) view.findViewById(adView);
        }

        public void bind(){
            AdRequest request = new AdRequest.Builder()
                    .addTestDevice("B61303E09133F379EC813EB0383AECBE")
                    .build();
            nativeExpressAdView.loadAd(request);
        }
    }


    public void updateData(int totalItem){
        mMovieList = new ArrayList<>();

        int maxItemIndex;
        if(mAllMovieList.size() < totalItem){
            maxItemIndex = ((int)(Math.floor(mAllMovieList.size() / 3))) * 3;
        }else{
            maxItemIndex = totalItem;
        }

        for(int i = 0; i < maxItemIndex; i++){
            mMovieList.add(mAllMovieList.get(i));
        }

        super.notifyDataSetChanged();
    }

}
