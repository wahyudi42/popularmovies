package id.co.lazystudio.watchIt_freemoviedatabase.utils;

import android.support.design.widget.FloatingActionButton;

/**
 * Created by faqiharifian on 03/10/16.
 */
public class FabVisibilityChangeListener extends FloatingActionButton.OnVisibilityChangedListener{
    private boolean fabShouldBeShown;
    public FabVisibilityChangeListener(){
    }

    public void setFabShouldBeShown(boolean fabShouldBeShown) {
        this.fabShouldBeShown = fabShouldBeShown;
    }

    @Override
    public void onShown(FloatingActionButton fab) {
        super.onShown(fab);
        if(!fabShouldBeShown){
            fab.hide();
        }
    }

    @Override
    public void onHidden(FloatingActionButton fab) {
        super.onHidden(fab);
        if(fabShouldBeShown){
            fab.show();
        }
    }
}
