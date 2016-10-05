package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import id.co.lazystudio.watchIt_freemoviedatabase.R;
import id.co.lazystudio.watchIt_freemoviedatabase.ShowImageActivity;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;

/**
 * Created by faqiharifian on 05/10/16.
 */

public class ListBackdropAdapter extends RecyclerView.Adapter<ListBackdropAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Image> mImageList;

    public ListBackdropAdapter(Context context, ArrayList<Image> images){
        mContext = context;
        mImageList = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_backdrop, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            view.setBackgroundColor(mContext.getResources().getColor(android.R.color.white, mContext.getTheme()));
        else
            view.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));

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

        public void bind(final Image image){
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

            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(mContext, ImageViewActivity.class);
//                    i.putExtra(ImageViewActivity.KEY_IMAGE, image.getBackdropPath(mContext, -1));
//                    i.putExtra(ImageViewActivity.KEY_IMAGE_ERROR, R.drawable.no_image_land);
//                    mContext.startActivity(i);
//                    showImage(image.getBackdropPath(mContext, -1));
                    Intent i = new Intent(mContext, ShowImageActivity.class);
                    i.putParcelableArrayListExtra(ShowImageActivity.KEY_IMAGE_LIST, mImageList);
                    i.putExtra(ShowImageActivity.KEY_TYPE, ShowImageActivity.TYPE_BACKDROP);
                    mContext.startActivity(i);
                }
            });
        }
    }

    public void showImage(String imageURL) {
        Dialog builder = new Dialog(mContext);
//        builder.setContentView(R.layout.activity_image_view);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        ImageView imageView = new ImageView(mContext);
        Picasso.with(mContext)
                .load(imageURL)
                .into(imageView);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }
}
