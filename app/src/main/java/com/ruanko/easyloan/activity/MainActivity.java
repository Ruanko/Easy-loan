package com.ruanko.easyloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.adapter.MainFragmentPagerAdapter;
import com.ruanko.easyloan.fragment.AccountFragment;
import com.ruanko.easyloan.fragment.HomeFragment;
import com.ruanko.easyloan.fragment.OrderFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    // For tab layout
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton floatButtonAddOrder;
    private FloatingActionButton floatButtonModifyInfo;

    // For drawer
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initTabLayout();
        initFloatingActionButton();
        selectTab();
    }

    private void selectTab () {
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            try {
                if (intent.getStringExtra(Intent.EXTRA_TEXT).equals("account")) {
                    tabLayout.getTabAt(2).select();
                }
                else if (intent.getStringExtra(Intent.EXTRA_TEXT).equals("order")) {
                    tabLayout.getTabAt(1).select();
                }
                else {
                    tabLayout.getTabAt(0).select();
                }
            }
            catch (NullPointerException e) {
            }

        }
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

    // floating action button
    private void initFloatingActionButton() {
        this.floatButtonAddOrder = (FloatingActionButton) findViewById(R.id.fab_main_add_order);
        this.floatButtonModifyInfo = (FloatingActionButton) findViewById(R.id.fab_main_modify_info);
        // 只在第二个Tab显示浮动按钮
        this.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    floatButtonAddOrder.show();
                    floatButtonModifyInfo.hide();
                } else if (position == 2) {
                    floatButtonModifyInfo.show();
                    floatButtonAddOrder.hide();
                } else {
                    floatButtonModifyInfo.hide();
                    floatButtonAddOrder.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        floatButtonAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.snack_bar_main_info), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.snack_bar_main_action), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
            }
        });
        floatButtonModifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, getString(R.string.snack_bar_main_info), Snackbar.LENGTH_LONG)
                        .setAction(getString(R.string.snack_bar_main_action), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
            }
        });
    }

    // for tab layout
    private void initTabLayout() {
        viewPager = (ViewPager) findViewById(R.id.view_pager_main);
        //initViewPager(viewPager);

        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getString(R.string.tab_home));
        adapter.addFragment(new OrderFragment(), getString(R.string.tab_orders));
        adapter.addFragment(new AccountFragment(), getString(R.string.tab_account));
        viewPager.setAdapter(adapter);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
        tabLayout.setupWithViewPager(viewPager);
    }

    // For navigation
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
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

    // check if login
    private void checkAccountState () {
        AVUser user = AVUser.getCurrentUser();
        if (user != null) {
            TextView navHeadText = (TextView) findViewById(R.id.text_nav_header);
            navHeadText.setText(user.getUsername());
        }
    }

}