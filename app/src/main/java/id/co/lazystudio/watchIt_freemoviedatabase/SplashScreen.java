package id.co.lazystudio.watchIt_freemoviedatabase;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.Toast;

import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbClient;
import id.co.lazystudio.watchIt_freemoviedatabase.connection.TmdbService;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Configuration;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.DefaultPreferences;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.TmdbConfigurationPreference;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final DefaultPreferences defaultPref = new DefaultPreferences(this);
        if(defaultPref.isFirstRun()){
            if(Utils.isInternetConnected(this)) {
                TmdbService tmdbService =
                        TmdbClient.getClient().create(TmdbService.class);

                final Call<Configuration> configuration = tmdbService.getConfiguration();

                configuration.enqueue(new Callback<Configuration>() {
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
                alertDialog.setTitle("No Internet");
                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Toast.makeText(getApplicationContext(), "OK Button pressed!",
                                Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        }else{
            new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, 50);
        }


    }
}
