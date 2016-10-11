package id.co.lazystudio.watchIt_freemoviedatabase;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbClient;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbService;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Configuration;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.DefaultPreferences;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.Utils;
import retrofit2.Call;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    int colorFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                colorFilter = getResources().getColor(R.color.colorPrimary, null);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                colorFilter = getResources().getColor(R.color.colorPrimary);
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }

        final ImageView background = (ImageView)findViewById(R.id.splash_background);


        Picasso.with(this)
                .load(R.drawable.splash_background)
                .fit()
                .centerCrop()
                .into(background, new Callback() {
                    @Override
                    public void onSuccess() {
                        background.setColorFilter(Utils.getColorWithAlpha((float)0.5, colorFilter));
                        findViewById(R.id.container).setVisibility(View.VISIBLE);
                        loadData();
                    }

                    @Override
                    public void onError() {

                    }
                });
    }

    private void loadData(){
        final DefaultPreferences defaultPref = new DefaultPreferences(this);
        if(defaultPref.isFirstRun()){
            if(Utils.isInternetConnected(this)) {
                TmdbService tmdbService =
                        TmdbClient.getClient().create(TmdbService.class);

                final Call<Configuration> configuration = tmdbService.getConfiguration();

                configuration.enqueue(new retrofit2.Callback<Configuration>() {
                    @Override
                    public void onResponse(Call<Configuration> call, Response<Configuration> response) {
                        TmdbConfigurationPreference pref = new TmdbConfigurationPreference(SplashScreen.this);
                        pref.setConfiguration(response.body());

                        defaultPref.setSecondRun();
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    }

                    @Override
                    public void onFailure(Call<Configuration> call, Throwable t) {

                    }
                });
            }else{
                Dialog dialog = new Dialog(this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle(getResources().getString(R.string.splash_no_internet_title_dialog));
                alertDialog.setMessage(getResources().getString(R.string.splash_no_internet_message_dialog));
                alertDialog.setPositiveButton(getResources().getString(R.string.splash_no_internet_positive_dialog), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton(getResources().getString(R.string.splash_no_internet_negative_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, 2000);
        }
    }
}
