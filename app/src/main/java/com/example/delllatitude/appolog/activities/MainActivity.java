package com.example.delllatitude.appolog.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.delllatitude.appolog.R;
import com.example.delllatitude.appolog.activities.SearchableActivity;
import com.example.delllatitude.appolog.fragments.HomeFragment;
import com.example.delllatitude.appolog.fragments.WriteFragment;
import com.example.delllatitude.appolog.fragments.YouFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemReselectedListener{

    private BottomNavigationView mbottomNavigationView;
    private Toolbar toolbar;
    private ImageView searchImageView;
//    private SearchView searchView;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar =findViewById(R.id.toolbar);

        mbottomNavigationView = findViewById(R.id.bottomNavigation);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        mbottomNavigationView.setOnNavigationItemSelectedListener(this);
//        mbottomNavigationView.setOnNavigationItemReselectedListener(this);
        mbottomNavigationView.setSelectedItemId(R.id.navigation_home);

        actionBar.setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                RecyclerView recyclerView = findViewById(R.id.rvLatest);
                if(recyclerView==null){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, new HomeFragment()).commit();
                }else{
                  recyclerView.smoothScrollToPosition(0);
                }
                return true;

            case R.id.navigation_create_blog:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new WriteFragment()).commit();
                return true;

            case R.id.navigation_You:
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, new YouFragment()).commit();
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();

        // Assumes current activity is the searchable activity
        ComponentName cn = new ComponentName(this, SearchableActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setQueryHint("Search blogs by title");
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setBackgroundColor(Color.WHITE);
//                searchView.setMinimumWidth(Resources.getSystem().getDisplayMetrics().widthPixels);
                findViewById(R.id.tvAppTitle).setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimary, null));
                findViewById(R.id.tvAppTitle).setVisibility(View.VISIBLE);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.notifications:
//                launch new notifications activity
//                return true;
            case R.id.searchView:
                return true;
//            case R.id.settings:
////                launch new settings activity
//                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

//// This function animates the click of the imageView of search
//    public void performSearch(View view) {
//        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
//        searchImageView.startAnimation(animFadein);
////        onSearchRequested();
//    }

    @Override
    public void onNavigationItemReselected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                RecyclerView recyclerView = findViewById(R.id.rvLatest);
                if(recyclerView!=null)
                recyclerView.scrollTo(0,0);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragmentContainer, new HomeFragment()).commit();
//            case R.id.navigation_create_blog:
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragmentContainer, new WriteFragment()).commit();
//            case R.id.navigation_You:
////                new YouFragment();
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragmentContainer, new HomeFragment()).commit();
        }
    }
}
