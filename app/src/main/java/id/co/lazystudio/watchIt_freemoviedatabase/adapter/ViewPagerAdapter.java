package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.R;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by faqiharifian on 05/10/16.
 */

public class ViewPagerAdapter extends PagerAdapter {
    public static final String TYPE_BACKDROP = "backdrop";
    public static final String TYPE_POSTER = "poster";
    private List<Image> mImageList;
    private Context mContext;
    private String mType;
    Dialog dialog;

    public ViewPagerAdapter(Context context, List<Image> images, String type, Dialog dialog){
        mContext = context;
        mImageList = images;
        mType = type;


//        int transparent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
//            transparent = mContext.getResources().getColor(android.R.color.transparent, mContext.getTheme());
//        else
//            transparent = mContext.getResources().getColor(android.R.color.transparent);

        this.dialog = dialog;
//        dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.progress_dialog);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(transparent));
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
//        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_show_image, container, false);

//        ViewPager.LayoutParams params = new ViewPager.LayoutParams()
        PhotoView photoView = new PhotoView(container.getContext());
//        PhotoView photoView = (PhotoView) view.findViewById(R.id.show_image_photoview);
//        final ProgressBar imageProgressBar = (ProgressBar) view.findViewById(R.id.show_image_progressbar);


        container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);

        String imageUrl = mType.equals(TYPE_BACKDROP) ?
                mImageList.get(position).getBackdropPath(mContext, -1) :
                mImageList.get(position).getPosterPath(mContext, -1);

        int imageErrorUrl = mType.equals(TYPE_BACKDROP) ?
                R.drawable.no_image_land :
                R.drawable.no_image_port;

        Log.e("image url", imageUrl);

        Picasso.with(mContext)
                .load(imageUrl)
                .error(imageErrorUrl)
                .into(photoView, new Callback() {
                    @Override
                    public void onSuccess() {
                        attacher.update();
//                        imageProgressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                    }

                    @Override
                    public void onError() {
                        attacher.update();
//                        imageProgressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });

//        return view;
        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
