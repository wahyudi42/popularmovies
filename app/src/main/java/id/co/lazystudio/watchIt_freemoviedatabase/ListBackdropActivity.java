package id.co.lazystudio.watchIt_freemoviedatabase;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.List;

import id.co.lazystudio.watchIt_freemoviedatabase.adapter.ListBackdropAdapter;
import id.co.lazystudio.watchIt_freemoviedatabase.entity.Image;

public class ListBackdropActivity extends AppCompatActivity {
    public static final String KEY_BACKDROP_LIST = "backdrop_list";
    public static final String KEY_TITLE = "title";
    private RecyclerView mListBackdropRecyclerView;
    private ListBackdropAdapter mListBackdropAdapter;
    private List<Image> mBackdropList;

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

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        mListBackdropAdapter = new ListBackdropAdapter(this, mBackdropList);

        mListBackdropRecyclerView.setAdapter(mListBackdropAdapter);

        mListBackdropRecyclerView.setLayoutManager(layoutManager);
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
