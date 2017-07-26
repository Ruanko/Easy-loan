package com.ruanko.easyloan.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.adapter.MainFragmentPagerAdapter;
import com.ruanko.easyloan.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    // For tab layout
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar toolbar;

    // For page start
    private boolean isShowPageStart = true;
    private ImageView imgViewPageStart;
    private RelativeLayout relativeLayoutPageStart;

    // For drawer
    private DrawerLayout drawerLayout;
    private final int MESSAGE_SHOW_DRAWER_LAYOUT = 0x001;
    private final int MESSAGE_SHOW_START_PAGE = 0x002;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_SHOW_DRAWER_LAYOUT:
                    drawerLayout.openDrawer(GravityCompat.START);
                    SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isFirst", false);
                    editor.apply();
                    break;

                case MESSAGE_SHOW_START_PAGE:
                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setDuration(300);
                    alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            relativeLayoutPageStart.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    relativeLayoutPageStart.startAnimation(alphaAnimation);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTabLayout();
        initPageStart();
    }
    
    // for other views
    
    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        LinearLayout nav_header = (LinearLayout) headerView.findViewById(R.id.nav_header);
        nav_header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    private void initPageStart () {
        relativeLayoutPageStart = (RelativeLayout) findViewById(R.id.relative_main);
        imgViewPageStart = (ImageView) findViewById(R.id.img_page_start);
        SharedPreferences sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);

        if (isShowPageStart) {
            relativeLayoutPageStart.setVisibility(View.VISIBLE);
            Glide.with(MainActivity.this).load(R.drawable.ic_launcher_big).into(imgViewPageStart);
            if (sharedPreferences.getBoolean("isFirst", true)) {
                mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_START_PAGE, 2000);
            } else {
                mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_START_PAGE, 1000);
            }
            isShowPageStart = false;
        }

        if (sharedPreferences.getBoolean("isFirst", true)) {
            mHandler.sendEmptyMessageDelayed(MESSAGE_SHOW_DRAWER_LAYOUT, 2500);
        }
    }

    // for tab layout
    private void initTabLayout() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.view_pager_main);
        initViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        tabLayout.setupWithViewPager(viewPager);
    }


    // for tab layout
    private void initViewPager(ViewPager viewPager) {
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getString(R.string.tab_home));
        adapter.addFragment(new HomeFragment(), getString(R.string.tab_orders));
        adapter.addFragment(new HomeFragment(), getString(R.string.tab_account));
        viewPager.setAdapter(adapter);
    }


    // For navigation
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = new Intent();

        switch (item.getItemId()) {
            case R.id.nav_item1:
//                intent.setClass(this, RecyclerViewActivity.class);
//                startActivity(intent);
                break;

            case R.id.nav_item2:
//                startActivity(intent);
                break;

            case R.id.nav_item3:
//                startActivity(intent);
                break;

            case R.id.nav_item4:
//                intent.setClass(this, BottomNavigationActivity.class);
//                startActivity(intent);
                break;
            case R.id.nav_setting:
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
            default:
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_menu_main_1:
                Intent intent = new Intent();

        }
        return super.onOptionsItemSelected(item);
    }
}