package id.co.lazystudio.popularmovies.utils;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import id.co.lazystudio.popularmovies.R;

/**
 * Created by faqiharifian on 25/09/16.
 */
public class Utils {
    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void setProcessComplete(View view){
        view.setVisibility(View.GONE);
    }

    public static void setProcessError(Context context, View view, int type){
        String text;
        TextView textView = (TextView) view;
        switch (type){
            case 400:
                text = context.getResources().getString(R.string.error_server_error);
                break;
            case 200:
                text = context.getResources().getString(R.string.error_not_found);
                textView.setBackgroundColor(Color.TRANSPARENT);
                textView.setTextColor(Utils.getColorWithAlpha(0.87f, Color.BLACK));
                break;
            default:
                text = context.getResources().getString(R.string.error_no_internet);
                break;
        }
        if(view != null){
            view.setVisibility(View.VISIBLE);
            ((TextView) view).setText(text);
        }
        Log.e("connection error", text);
    }

    public static int getColorWithAlpha(float alpha, int baseColor) {
        int a = Math.min(255, Math.max(0, (int) (alpha * 255))) << 24;
        int rgb = 0x00ffffff & baseColor;
        return a + rgb;
    }
}
