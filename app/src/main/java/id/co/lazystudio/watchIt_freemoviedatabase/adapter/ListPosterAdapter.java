package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.R;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;

/**
 * Created by faqiharifian on 05/10/16.
 */

public class ListPosterAdapter extends RecyclerView.Adapter<ListPosterAdapter.ViewHolder>{
    private List<Image> mImageList;
    private Context mContext;

    public ListPosterAdapter(Context context, List<Image> images){
        mContext = context;
        mImageList = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);
        final ImageView imageView = (ImageView) v.findViewById(R.id.poster_imageview);

        imageView.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout posterRelativeLayout = (RelativeLayout) imageView.getParent();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        posterRelativeLayout.getWidth() * 3 / 2
                );
                imageView.setLayoutParams(params);
            }
        });
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = mImageList.get(position);
        holder.bind(image);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView posterImageView;
        private ProgressBar posterProgressBar;
        private View parentView;
        public ViewHolder(View view){
            super(view);
            parentView = view;
            posterImageView = (ImageView) view.findViewById(R.id.poster_imageview);
            posterProgressBar = (ProgressBar) view.findViewById(R.id.poster_progressbar);
        }

        public void bind(Image image){
            Picasso.with(mContext)
                    .load(image.getPosterPath(mContext, 0))
                    .error(R.drawable.no_image_port)
                    .fit()
                    .centerCrop()
                    .into(posterImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            posterProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            posterProgressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
