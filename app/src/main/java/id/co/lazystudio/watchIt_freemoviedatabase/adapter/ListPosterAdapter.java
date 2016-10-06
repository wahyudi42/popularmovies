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

public class ListPosterAdapter extends RecyclerView.Adapter<ListPosterAdapter.ViewHolder>{
    private ArrayList<Image> mImageList;
    private Context mContext;

    public ListPosterAdapter(Context context, ArrayList<Image> images){
        mContext = context;
        mImageList = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poster, parent, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            v.setBackgroundColor(mContext.getResources().getColor(android.R.color.white, mContext.getTheme()));
        else
            v.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));

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

        public void bind(final Image image){
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

            parentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent i = new Intent(mContext, ImageViewActivity.class);
//                    i.putExtra(ImageViewActivity.KEY_IMAGE, image.getPosterPath(mContext, -1));
//                    i.putExtra(ImageViewActivity.KEY_IMAGE_ERROR, R.drawable.no_image_port);
//                    mContext.startActivity(i);
//                    showImage(image.getPosterPath(mContext, -1));
                    Intent i = new Intent(mContext, ShowImageActivity.class);
                    i.putParcelableArrayListExtra(ShowImageActivity.KEY_IMAGE_LIST, mImageList);
                    i.putExtra(ShowImageActivity.KEY_IMAGE, image);
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
