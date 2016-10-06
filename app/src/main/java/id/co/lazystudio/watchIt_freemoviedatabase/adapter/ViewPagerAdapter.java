package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

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

    public ViewPagerAdapter(Context context, List<Image> images, String type){
        mContext = context;
        mImageList = images;
        mType = type;
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
                    }

                    @Override
                    public void onError() {
                        attacher.update();
//                        imageProgressBar.setVisibility(View.GONE);
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
