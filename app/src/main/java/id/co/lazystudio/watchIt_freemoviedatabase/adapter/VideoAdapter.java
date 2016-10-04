package id.co.lazystudio.watchIt_freemoviedatabase.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.R;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Video;

/**
 * Created by faqiharifian on 01/10/16.
 */
public class VideoAdapter extends BaseAdapter {
    Context mContext;
    List<Video> mVideoList;

    public VideoAdapter(Context context, List<Video> videos){
        mContext = context;
        mVideoList = videos;
    }
    @Override
    public int getCount() {
        return mVideoList.size();
    }

    @Override
    public Object getItem(int i) {
        return mVideoList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.item_poster, viewGroup, false);


        GridView.LayoutParams params = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                GridView.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(params);

        final Video video = mVideoList.get(i);

        Picasso.with(mContext)
                .load(video.getImageUrl())
                .error(R.drawable.no_image_land)
                .into((ImageView) v.findViewById(R.id.poster_imageview), new Callback() {
                    @Override
                    public void onSuccess() {
                        v.findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        v.findViewById(R.id.poster_progressbar).setVisibility(View.GONE);
                    }
                });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + video.getKey()));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v=" + video.getKey()));
                try {
                    mContext.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    mContext.startActivity(webIntent);
                }
            }
        });

        return v;
    }
}
