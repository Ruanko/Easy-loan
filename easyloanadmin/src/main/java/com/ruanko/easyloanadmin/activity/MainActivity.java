package com.ruanko.easyloanadmin.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.ruanko.easyloanadmin.R;
import com.ruanko.easyloanadmin.adapter.MainFragmentPagerAdapter;
import com.ruanko.easyloanadmin.data.SettingsContract;
import com.ruanko.easyloanadmin.data.UserContract;
import com.ruanko.easyloanadmin.fragment.AccountFragment;
import com.ruanko.easyloanadmin.fragment.OrderListFragment;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.tiancaicc.springfloatingactionmenu.MenuItemView;
import com.tiancaicc.springfloatingactionmenu.OnMenuActionListener;
import com.tiancaicc.springfloatingactionmenu.SpringFloatingActionMenu;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import static com.ruanko.easyloanadmin.utilities.FileUtils.getFileBytes;
import static com.ruanko.easyloanadmin.utilities.ImageUtils.getRoundedTransformation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,
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
    private EditText mBannerTitleEditText;
    private byte[] mImageBytes;
    private Button selectPicButton;
    private ImageView previewImageView;

    // bottom navigation
//    private BottomNavigationViewEx mBottomNavigationViewEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 警告：以下耦合度极其高，注意调用顺序
        initView();
        initNavigation();
//        initTabLayout();
//        initFloatingActionButton();
        initFab();
        initOtherView();
    }
    // for some views

    private void initView() {
        mTabLayoutForOrderList = (TabLayout) findViewById(R.id.tab_layout_main);
//        mBottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
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
                    mFloatButtonAddOrder.hide();
                    mFloatButtonModifyInfo.hide();
                    mTabLayoutForOrderList.setVisibility(View.VISIBLE);
                    mToolbar.setVisibility(View.GONE);
                } else if (position == 2) {
                    mFloatButtonModifyInfo.show();
                    mFloatButtonAddOrder.hide();
                    mTabLayoutForOrderList.setVisibility(View.VISIBLE);
                    mToolbar.setVisibility(View.VISIBLE);
                } else {
                    mTabLayoutForOrderList.setVisibility(View.VISIBLE);
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
//    private void initTabLayout() {
//        mViewPager = (ViewPager) findViewById(R.id.view_pager_main);
//        //initViewPager(mViewPager);
//
//        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
////        adapter.addFragment(new HomeFragment(), getString(R.string.tab_home));
//        adapter.addFragment(new OrderFragment(), getString(R.string.tab_orders));
////        adapter.addFragment(new AccountFragment(), getString(R.string.tab_account));
//        mViewPager.setAdapter(adapter);
//        mBottomNavigationViewEx.setupWithViewPager(mViewPager);
////        mTabLayout = (TabLayout) findViewById(R.id.tab_layout_main);
////        mTabLayout.setupWithViewPager(mViewPager);
//    }

    // For navigation
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        if (springFloatingActionMenu.isMenuOpen()) {
            springFloatingActionMenu.hideMenu();
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
            case R.id.nav_setting:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_about_us:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent.setClass(this, AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_user_setting:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                intent.setClass(this, UserInfoActivity.class);
                startActivity(intent);
                break;
            default:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.action_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_menu_main_1:
//                startActivity(new Intent(this, ApplyActivity.class));
                return false;
            case R.id.action_menu_account1:
                return false;
            case R.id.action_menu_account2:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnUserInfoChanged() {
//        Toast.makeText(this, "界面已更新", Toast.LENGTH_LONG).show();
        initNavigation();
    }

    private SpringFloatingActionMenu springFloatingActionMenu;

    private void initFab() {
        final com.melnykov.fab.FloatingActionButton fab = new com.melnykov.fab.FloatingActionButton(this);
        fab.setType(com.melnykov.fab.FloatingActionButton.TYPE_NORMAL);
//        fab.setImageDrawable(frameAnim);
        fab.setImageResource(R.drawable.ic_send_white_24dp);
        fab.setColorPressedResId(R.color.colorPrimary);
        fab.setColorNormalResId(R.color.colorAccent);
        fab.setColorRippleResId(R.color.colorAccent);
        fab.setShadow(true);
        springFloatingActionMenu = new SpringFloatingActionMenu.Builder(this)
                .fab(fab)
                .addMenuItem(R.color.photo, R.drawable.ic_create_white_24dp, "更新公告", R.color.text_color, this)
                .addMenuItem(R.color.chat, R.drawable.ic_account_circle_white_24dp, "用户管理", R.color.text_color, this)
                .addMenuItem(R.color.quote, R.drawable.ic_access_time_black_24dp, "逾期管理", R.color.text_color, this)
                .addMenuItem(R.color.link, R.drawable.ic_update_black_18dp, "系统设置", R.color.text_color, this)
                .addMenuItem(R.color.audio, R.drawable.ic_send_white_24dp, "信誉评估", R.color.text_color, this)
                .addMenuItem(R.color.text, R.drawable.ic_share_white_24dp, "分享", R.color.text_color, this)
                .addMenuItem(R.color.video, R.drawable.ic_account_balance_white_24dp, "资金结算", R.color.text_color, this)
                .animationType(SpringFloatingActionMenu.ANIMATION_TYPE_TUMBLR)
                .revealColor(R.color.colorPrimary)
                .gravity(Gravity.RIGHT | Gravity.BOTTOM)
                .onMenuActionListner(new OnMenuActionListener() {
                    @Override
                    public void onMenuOpen() {
                        fab.setImageResource(R.drawable.ic_close_white_24dp);
//                        fab.setImageDrawable(frameAnim);
//                        frameReverseAnim.stop();
//                        frameAnim.start();
                    }

                    @Override
                    public void onMenuClose() {
                        fab.setImageResource(R.drawable.ic_send_white_24dp);
//                        fab.setImageDrawable(frameReverseAnim);
//                        frameAnim.stop();
//                        frameReverseAnim.start();
                    }
                })
                .build();
    }


    @Override
    public void onClick(View v) {
        Log.d("TAG eg", "onclick");
        MenuItemView menuItemView = (MenuItemView) v;
        Toast.makeText(this, menuItemView.getLabelTextView().getText(), Toast.LENGTH_SHORT).show();
        if (menuItemView.getLabelTextView().getText().equals("更新公告")){
            updateBanner();
        }
    }

    private void initOtherView() {
        mViewPager = (ViewPager) findViewById(R.id.view_pager_main);
        MainFragmentPagerAdapter adapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new HomeFragment(), getString(R.string.tab_home));
//        adapter.addFragment(new OrderFragment(), getString(R.string.tab_orders));
//        adapter.addFragment(new AccountFragment(), getString(R.string.tab_account));
//        OrderFragmentPagerAdapter adapter = new OrderFragmentPagerAdapter(getChildFragmentManager());
        OrderListFragment fragment1 = new OrderListFragment();
        OrderListFragment fragment2 = new OrderListFragment();
        OrderListFragment fragment3 = new OrderListFragment();
        OrderListFragment fragment4 = new OrderListFragment();
        OrderListFragment fragment5 = new OrderListFragment();
        Bundle bundle1 = new Bundle();
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = new Bundle();
        Bundle bundle4 = new Bundle();
        Bundle bundle5 = new Bundle();
        bundle1.putInt("tab_at", 0);
        bundle2.putInt("tab_at", 1);
        bundle3.putInt("tab_at", 2);
        bundle4.putInt("tab_at", 3);
        bundle5.putInt("tab_at", 4);
        fragment1.setArguments(bundle1);
        fragment2.setArguments(bundle2);
        fragment3.setArguments(bundle3);
        fragment4.setArguments(bundle4);
        fragment5.setArguments(bundle5);
        adapter.addFragment(fragment1, getString(R.string.tab_all_order));
        adapter.addFragment(fragment3, getString(R.string.tab_apply_check));
        adapter.addFragment(fragment2, getString(R.string.tab_repay_request));
        adapter.addFragment(fragment4, getString(R.string.tab_finished_order));
        adapter.addFragment(fragment5, getString(R.string.tab_overdue_order));
        mViewPager.setAdapter(adapter);
        mTabLayoutForOrderList.setupWithViewPager(mViewPager);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            try {
                mImageBytes = getFileBytes(getContentResolver()
                        .openInputStream(result.getUri()));
                AVObject avObject = new AVObject(SettingsContract.BannerEntry.TABLE_NAME);
                avObject.put(SettingsContract.BannerEntry.COLUMN_TITLE, mBannerTitleEditText.getText().toString());
                avObject.put(SettingsContract.BannerEntry.COLUMN_IMAGE, new AVFile("banner.jpg", mImageBytes));
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Picasso.with(MainActivity.this).load(result.getUri()).into(previewImageView);
                            selectPicButton.setText("上传完成，点击继续选择图片");
                            Toast.makeText(MainActivity.this, getString(R.string.upload_done), Toast.LENGTH_LONG).show();
                        } else {
                            String json = e.getMessage();
                            JSONTokener tokener = new JSONTokener(json);
                            try {
                                JSONObject jsonObject = (JSONObject) tokener.nextValue();
                                Toast.makeText(MainActivity.this,
                                        jsonObject.getString("error"),
                                        Toast.LENGTH_LONG).show();
                            } catch (JSONException jse) {
                                jse.printStackTrace();
                            }
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateBanner() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        final View dialogView = MainActivity.this.getLayoutInflater().inflate(R.layout.dialog_upload, null);
        Button btn_dialog_bottom_sheet_ok = (Button) dialogView.findViewById(R.id.btn_dialog_bottom_sheet_ok);
        mBannerTitleEditText = (EditText) dialogView.findViewById(R.id.et_banner_title);
        bottomSheetDialog.setContentView(dialogView);
        previewImageView = (ImageView) dialogView.findViewById(R.id.img_prepare_upload);
        selectPicButton = (Button) dialogView.findViewById(R.id.btn_select_pic);
        selectPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setActivityTitle(getString(R.string.crop_image_title))
                        .setOutputCompressQuality(90)
                        .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                        .start(MainActivity.this);
            }
        });
        btn_dialog_bottom_sheet_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();

    }
}