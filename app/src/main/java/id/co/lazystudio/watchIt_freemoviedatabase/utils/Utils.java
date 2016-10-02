package id.co.lazystudio.watchIt_freemoviedatabase.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.parser.ErrorParser;

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

    public static void setProcessComplete(View view, List<String> processList, String process){
        processList.remove(process);
        if(processList.isEmpty())
            setProcessComplete(view);
    }

    public static void setProcessError(View view, String error){
        if(view != null){
            view.setVisibility(View.VISIBLE);
            ((TextView) view).setText(error);
        }
        Log.e("connection error", error);
    }

    public static void setProcessError(View view, ErrorParser error){
        if(view != null){
            view.setVisibility(View.VISIBLE);
            ((TextView) view).setText("Server Error Occured");
        }
        Log.e("connection error", "Server Error Occured");
        Log.e("connection error", String.valueOf(error.getStatusCode())+" - "+String.valueOf(error.getStatusMessage()));
    }
}
