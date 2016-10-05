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

public class ListBackdropAdapter extends RecyclerView.Adapter<ListBackdropAdapter.ViewHolder>{
    private Context mContext;
    private List<Image> mImageList;

    public ListBackdropAdapter(Context context, List<Image> images){
        mContext = context;
        mImageList = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_backdrop, parent, false);
        final ImageView backdropImageView = (ImageView) view.findViewById(R.id.backdrop_imageview);

        backdropImageView.post(new Runnable() {
            @Override
            public void run() {
                RelativeLayout backdropRelativeLayout = (RelativeLayout) backdropImageView.getParent();
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        backdropRelativeLayout.getWidth() * 9 / 16
                );
                backdropImageView.setLayoutParams(params);
            }
        });

        return new ViewHolder(view);
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
        private View parentView;
        private ImageView backdropImageView;
        private ProgressBar backdropProgressBar;

        public ViewHolder(View view){
            super(view);
            parentView = view;
            backdropImageView = (ImageView) view.findViewById(R.id.backdrop_imageview);
            backdropProgressBar = (ProgressBar) view.findViewById(R.id.backdrop_progressbar);
        }

        public void bind(Image image){
            Picasso.with(mContext)
                    .load(image.getBackdropPath(mContext, 0))
                    .error(R.drawable.no_image_land)
                    .fit()
                    .centerCrop()
                    .into(backdropImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            backdropProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            backdropProgressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
