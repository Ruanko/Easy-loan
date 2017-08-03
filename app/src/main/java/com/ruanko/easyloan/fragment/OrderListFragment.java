package com.ruanko.easyloan.fragment;

/**
 * Created by deserts on 17/7/25.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.activity.ApplyActivity;
import com.ruanko.easyloan.adapter.OrderListAdapter;
import com.ruanko.easyloan.data.OrderContract;
import com.ruanko.easyloan.data.SettingsContract;
import com.squareup.picasso.Picasso;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrderListFragment extends Fragment
{
    private SwipeRefreshLayout mRootView;
    private RecyclerView mRecyclerView;
    private List<AVObject> mOrderList = new ArrayList<>();
    OrderListAdapter mOrderListAdapter;
    private FloatingActionButton mFloatingActionButton;
    private TabLayout mTabLayout;
    private Banner banner;
    private static final Animation dummyAnimation = new AlphaAnimation(1,1);
    static{
        dummyAnimation.setDuration(500);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mRootView = (SwipeRefreshLayout) inflater.inflate(
                R.layout.fragment_order_list, container, false);
        initView();
        loadData();
        return mRootView;
    }

    private void initView() {
        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout_for_orders);
        mOrderListAdapter = new OrderListAdapter(getContext(), mOrderList);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_order_list);
        mRecyclerView.setNestedScrollingEnabled(false);
        mFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_main_add_order);

        if (getScreenWidthDp() >= 1200) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else if (getScreenWidthDp() >= 800) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
        } else {
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);
        }

        mRecyclerView.setAdapter(mOrderListAdapter);
        mRootView.setColorSchemeResources(R.color.google_blue,
                R.color.google_green, R.color.google_red, R.color.google_yellow);
        mRootView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRootView.setRefreshing(false);
                    }
                }, 1000);

            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), ApplyActivity.class),
                        ApplyActivity.APPLY_ACTIVITY_REQUEST_CODE);

            }
        });


        banner = (Banner) mRootView.findViewById(R.id.banner);
    }


    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }


    private void loadData() {
        //  update user credit level

        int tabAt = (int) getArguments().get("tab_at");
        AVQuery<AVObject> avQuery1;
        avQuery1 = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);

        if (tabAt == 1) {
            AVQuery<AVObject> query1 = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);
            AVQuery<AVObject> query2 = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);
            query1.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.GRANT);
            query2.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.PARTIAL_REPAY);
            avQuery1 = AVQuery.or(Arrays.asList(query1, query2));
        }
        else if (tabAt == 2) {
            avQuery1.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.PENDING);
        }
        else if (tabAt == 3) {
            avQuery1.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.DONE);
        }
        else if (tabAt == 4) {
            avQuery1.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.OVERDUE);
        }
        AVQuery<AVObject> avQuery2 = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);
        avQuery2.whereEqualTo(OrderContract.OrderEntry.COLUMN_OWNER, AVUser.getCurrentUser());
        AVQuery<AVObject> avQuery = AVQuery.and(Arrays.asList(avQuery2, avQuery1));
        avQuery.orderByDescending(OrderContract.OrderEntry.COLUMN_CREATE_AT);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mOrderListAdapter.updateData(list);
                    mOrderListAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });

        if (tabAt == 0) {
            loadBanner();
        }
        else {
            banner.setVisibility(View.GONE);
        }
    }

    private void loadBanner() {
        banner.setVisibility(View.VISIBLE);
        //设置图片集合
        banner.setImageLoader(new OrderListFragment.GlideImageLoader());
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        final ArrayList images = new ArrayList<String>();
        final ArrayList<String> titles = new ArrayList<>();
        AVQuery<AVObject> avQuery = new AVQuery<>(SettingsContract.BannerEntry.TABLE_NAME);
        avQuery.orderByDescending(SettingsContract.BannerEntry.COLUMN_SERIAL);
        avQuery.limit(5);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject object : list){
                    AVFile imageFile = object.getAVFile(SettingsContract.BannerEntry.COLUMN_IMAGE);
                    if (imageFile != null) {
                        Log.d("", "Home fragment: get url = " + imageFile.getUrl());
                        images.add(imageFile.getUrl());
                        titles.add(object.getString(SettingsContract.BannerEntry.COLUMN_TITLE));
                    }
                }
                banner.setImages(images);
                banner.setBannerTitles(titles);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ApplyActivity.APPLY_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            loadData();
        }
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if(!enter && getParentFragment() != null){
            return dummyAnimation;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Picasso 加载图片简单用法
            Picasso.with(context).load((String)path).into(imageView);
        }
    }
}


