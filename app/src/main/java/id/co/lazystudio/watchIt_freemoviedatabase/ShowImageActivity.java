package id.co.lazystudio.watchIt_freemoviedatabase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.adapter.ViewPagerAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;
import id.co.lazystudio.watchIt_freemoviedatabase.view.HackyViewPager;

public class ShowImageActivity extends AppCompatActivity {
    public static final String KEY_IMAGE_LIST = "image_list";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_TYPE = "type";
    public static final String TYPE_BACKDROP = ViewPagerAdapter.TYPE_BACKDROP;
    public static final String TYPE_POSTER = ViewPagerAdapter.TYPE_POSTER;
    private List<Image> mImageList;
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    ViewPager imageViewPager;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_image);

        Bundle args = getIntent().getExtras();

        mImageList = args.getParcelableArrayList(KEY_IMAGE_LIST);
        String type = args.getString(KEY_TYPE);

        Image image = args.getParcelable(KEY_IMAGE);

        String imageUrl = type.equals(TYPE_BACKDROP) ?
                image.getBackdropPath(this, -1) :
                image.getPosterPath(this, -1);

        int imageErrorUrl = type.equals(TYPE_BACKDROP) ?
                R.drawable.no_image_land :
                R.drawable.no_image_port;

        mContentView = findViewById(R.id.image_viewpager);
//        mContentView = findViewById(R.id.show_image_photoview);

//        PhotoView photoView = (PhotoView) mContentView;
//
//        final PhotoViewAttacher attacher = new PhotoViewAttacher(photoView);
//
//        Picasso.with(this)
//                .load(imageUrl)
//                .error(imageErrorUrl)
//                .into(photoView, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        attacher.update();
//                    }
//
//                    @Override
//                    public void onError() {
//                        attacher.update();
//                    }
//                });


//        ViewPager imageViewPager = (ViewPager) mContentView;
        imageViewPager = (HackyViewPager) mContentView;

        int transparent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            transparent = this.getResources().getColor(android.R.color.transparent, this.getTheme());
        else
            transparent = this.getResources().getColor(android.R.color.transparent);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(transparent));

        ViewPagerAdapter imageAdapter = new ViewPagerAdapter(this, mImageList, type, dialog);

        imageViewPager.setAdapter(imageAdapter);

        imageViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                dialog.show();
                delayedHide(100);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
