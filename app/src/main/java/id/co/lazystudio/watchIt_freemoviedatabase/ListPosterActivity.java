package id.co.lazystudio.watchIt_freemoviedatabase;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

import id.co.lazystudio.watchIt_freemoviedatabase.adapter.ListPosterAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;

public class ListPosterActivity extends AppCompatActivity {
    public static final String KEY_POSTER_LIST = "poster_list";
    public static final String KEY_TITLE = "title";
    private RecyclerView mListPosterRecyclerView;
    private ListPosterAdapter mListPosterAdapter;
    private ArrayList<Image> mPosterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_poster);

        Bundle args = getIntent().getExtras();
        mPosterList = args.getParcelableArrayList(KEY_POSTER_LIST);

        String title = getResources().getString(R.string.title_activity_list_poster)+
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

        mListPosterRecyclerView = (RecyclerView) findViewById(R.id.list_poster_recyclerview);

        mListPosterAdapter = new ListPosterAdapter(this, mPosterList);

        mListPosterRecyclerView.setAdapter(mListPosterAdapter);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);

        mListPosterRecyclerView.setLayoutManager(layoutManager);
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
}
