package id.co.lazystudio.watchIt_freemoviedatabase.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;

import id.co.lazystudio.watchIt_freemoviedatabase.R;

/**
 * Created by faqiharifian on 25/09/16.
 */
public class MySliderView extends BaseSliderView {
    int mTag = 0;
    public MySliderView(Context context, int idTag){
        super(context);
        mTag = idTag;
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.item_slider,null);
        v.setTag(mTag);
        ImageView target = (ImageView)v.findViewById(R.id.daimajia_slider_image);
        TextView description = (TextView)v.findViewById(R.id.description);
        description.setText(getDescription());
        bindEventAndShow(v, target);
        return v;
    }
}
