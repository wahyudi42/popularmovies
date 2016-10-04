package id.co.lazystudio.watchIt_freemoviedatabase.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by faqiharifian on 04/10/16.
 */

public class PosterView extends ImageView {
    public PosterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public PosterView(Context context) {
        super(context);
    }
    public PosterView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height, width;
        if(widthMeasureSpec > heightMeasureSpec){
            width = MeasureSpec.getSize(widthMeasureSpec);
            height = width * 3 / 2;
        }else{
            height = MeasureSpec.getSize(heightMeasureSpec);
            width = height * 2 / 3;
        }
        setMeasuredDimension(width, height);
        super.onMeasure(width, height);
    }
}
