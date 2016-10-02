package id.co.lazystudio.watchIt_freemoviedatabase.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by faqiharifian on 25/09/16.
 */
public class DefaultPreferences {
    Context _context;

    private final String PREF_NAME = "default";
    private final String FIRST_RUN_KEY = "is_first_run";

    public DefaultPreferences(Context context){
        _context = context;
    }

    public boolean isFirstRun(){
        SharedPreferences pref = _context.getSharedPreferences(PREF_NAME, 0);
        return pref.getBoolean(FIRST_RUN_KEY, true);
    }

    public void setSecondRun(){
        SharedPreferences.Editor editor = _context.getSharedPreferences(PREF_NAME, 0).edit();
        editor.putBoolean(FIRST_RUN_KEY, false);
        editor.apply();
    }
}
