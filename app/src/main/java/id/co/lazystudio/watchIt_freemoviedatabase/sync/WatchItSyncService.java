package id.co.lazystudio.watchIt_freemoviedatabase.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class WatchItSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static WatchItSyncAdapter sWatchItSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("WatchItSyncService", "onCreate - WatchItSyncService");
        synchronized (sSyncAdapterLock) {
            if (sWatchItSyncAdapter == null) {
                sWatchItSyncAdapter = new WatchItSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sWatchItSyncAdapter.getSyncAdapterBinder();
    }
}