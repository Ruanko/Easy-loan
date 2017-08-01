package com.ruanko.easyloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.adapter.MainFragmentPagerAdapter;
import com.ruanko.easyloan.data.UserContract;
import com.ruanko.easyloan.fragment.AccountFragment;
import com.ruanko.easyloan.fragment.HomeFragment;
import com.ruanko.easyloan.fragment.OrderFragment;
import com.squareup.picasso.Picasso;

import static com.ruanko.easyloan.utilities.ImageUtils.getRoundedTransformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AccountFragment.UserInfoChangedListener {
    private Toolbar mToolbar;
    // For tab layout
    private TabLayout mTabLayoutForOrderList;
    private ViewPager mViewPager;
    private FloatingActionButton mFloatButtonAddOrder;
    private FloatingActionButton mFloatButtonModifyInfo;

    // For drawer
    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;

    // bottom navigation
    private BottomNavigationViewEx mBottomNavigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 警告：以下耦合度极其高，注意调用顺序
        initView();
        initNavigation();
        initTabLayout();
        initFloatingActionButton();
    }
    // for some views

    private void initView() {
        mTabLayoutForOrderList = (TabLayout) findViewById(R.id.tab_layout_for_orders);
        mBottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);


        mToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigation() {
        mNavigationView.setNavigationItemSelectedListener(this);
        View headerView = mNavigationView.getHeaderView(0);
        final LinearLayout navHeaderRoot = (LinearLayout) headerView.findViewById(R.id.nav_header);
        TextView navHeadName = (TextView) navHeaderRoot.findViewById(R.id.name_nav_header);
        TextView navHeadInfo = (TextView) navHeaderRoot.findViewById(R.id.info_nav_header);
        ImageView avatarImageView = (ImageView) navHeaderRoot.findViewById(R.id.round_image_view_head);
        AVUser avUser = AVUser.getCurrentUser();
        if (avUser != null) {
            navHeadName.setText(avUser.getUsername());
            navHeadInfo.setText(avUser.getMobilePhoneNumber());
            AVFile avFile = avUser.getAVFile(UserContract.UserEntry.COLUMN_AVATAR);
            if (avFile != null) {
                Picasso.with(this)
                        .load(avFile.getUrl())
                        .transform(getRoundedTransformation())
                        .fit()
                        .into(avatarImageView);
            } else {
                Picasso.with(this)
                        .load(R.drawable.default_header)
                        .transform(getRoundedTransformation())
                        .fit()
                        .into(avatarImageView);
            }

        } else {
            navHeadName.setText(getString(R.string.nav_header_text));
            navHeadInfo.setText(getString(R.string.nav_header_info));
        }
        avatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
//                mViewPager.getChildAt(2).setSelected(true);
            }
        });

    }

    // floating action button
    private void initFloatingActionButton() {
        this.mFloatButtonAddOrder = (FloatingActionButton) findViewById(R.id.fab_main_add_order);
        this.mFloatButtonModifyInfo = (FloatingActionButton) findViewById(R.id.fab_main_modify_info);
        // 只在第二个Tab显示浮动按钮
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    mFloatButtonAddOrder.show();
                    mFloatButtonModifyInfo.hide();
                    mTabLayoutForOrderList.setVisibility(View.VISIBLE);
                    mToolbar.setVisibility(View.GONE);
                } else if (position == 2) {
                    mFloatButtonModifyInfo.show();
                    mFloatButtonAddOrder.hide();
                    mTabLayoutForOrderList.setVisibility(View.GONE);
                    mToolbar.setVisibility(View.VISIBLE);
                } else {
                    mTabLayoutForOrderList.setVisibility(View.GONE);
                    mFloatButtonModifyInfo.hide();
                    mFloatButtonAddOrder.hide();
                    mToolbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    // for tab layout
    private void initTabLayout() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager_main);
        //initViewPager(mViewPager);

        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getString(R.string.tab_home));
        adapter.addFragment(new OrderFragment(), getString(R.string.tab_orders));
        adapter.addFragment(new AccountFragment(), getString(R.string.tab_account));
        mViewPager.setAdapter(adapter);
        mBottomNavigationViewEx.setupWithViewPager(mViewPager);
//        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
//        mTabLayout.setupWithViewPager(mViewPager);
    }

    // For navigation
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
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
                mDrawerLayout.closeDrawer(GravityCompat.START);
//                mTabLayout.getTabAt(0).select();
                break;

            case R.id.nav_item2:
                mDrawerLayout.closeDrawer(GravityCompat.START);
//                mTabLayout.getTabAt(1).select();
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

        mDrawerLayout.closeDrawer(GravityCompat.START);
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
                startActivity(new Intent(this, ApplyActivity.class));
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnUserInfoChanged() {
        Toast.makeText(this, "界面已更新", Toast.LENGTH_LONG).show();
        initNavigation();
    }

}