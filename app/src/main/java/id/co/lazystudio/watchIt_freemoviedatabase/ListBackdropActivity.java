package id.co.lazystudio.watchIt_freemoviedatabase;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import id.co.lazystudio.watchIt_freemoviedatabase.adapter.ListBackdropAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.FabVisibilityChangeListener;
import id.co.lazystudio.watchIt_freemoviedatabase.utils.Utils;

public class ListBackdropActivity extends AppCompatActivity {
    public static final String KEY_BACKDROP_LIST = "backdrop_list";
    public static final String KEY_TITLE = "title";
    private RecyclerView mListBackdropRecyclerView;
    private ListBackdropAdapter mListBackdropAdapter;
    private ArrayList<Image> mBackdropList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_backdrop);

        Bundle args = getIntent().getExtras();
        mBackdropList = args.getParcelableArrayList(KEY_BACKDROP_LIST);

        String title = getResources().getString(R.string.title_activity_list_backdrop)+
                " "+args.getString(KEY_TITLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, null));
                toolbar.setTitleTextColor(getResources().getColor(android.R.color.white, getTheme()));
            }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
            }
        }

        mListBackdropRecyclerView = (RecyclerView) findViewById(R.id.list_backdrop_recyclerview);

        populateView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    private void populateView(){
        if(mBackdropList.size() > 0){
            if(Utils.isInternetConnected(this)){
                mListBackdropRecyclerView.setVisibility(View.VISIBLE);

                mListBackdropAdapter = new ListBackdropAdapter(this, mBackdropList);

                mListBackdropRecyclerView.setAdapter(mListBackdropAdapter);

                GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

                mListBackdropRecyclerView.setLayoutManager(layoutManager);
            }else{
                setComplete(-1);
            }
        }else{
            setComplete(200);
        }

        if(Utils.isInternetConnected(this)) {
            Utils.initializeAd(this, findViewById(R.id.content_list_poster));
        }
    }

    private void setComplete(int error){
        mListBackdropRecyclerView.setVisibility(View.GONE);

        Utils.setProcessError(this, findViewById(R.id.notification_textview), error);
        if(error != 200) {
            final FloatingActionButton refreshFab = (FloatingActionButton) findViewById(R.id.refresh_fab);
            final FabVisibilityChangeListener fabListener = new FabVisibilityChangeListener();
            fabListener.setFabShouldBeShown(true);
            refreshFab.show(fabListener);
            refreshFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    findViewById(R.id.notification_textview).setVisibility(View.GONE);

                    fabListener.setFabShouldBeShown(false);
                    refreshFab.hide(fabListener);
                    populateView();
//                Intent i = getIntent();
//                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                finish();
//                startActivity(i);
                }
            });
        }
    }
}
