package com.ruanko.easyloanadmin.fragment;

/**
 * Created by deserts on 17/7/25.
 */

import android.app.Activity;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.ruanko.easyloanadmin.R;
import com.ruanko.easyloanadmin.activity.ApplyActivity;
import com.ruanko.easyloanadmin.adapter.OrderListAdapter;
import com.ruanko.easyloanadmin.data.OrderContract;

import java.util.ArrayList;
import java.util.List;

public class OrderListFragment extends Fragment
{
    private SwipeRefreshLayout mRootView;
    private RecyclerView mRecyclerView;
    private List<AVObject> mOrderList = new ArrayList<>();
    OrderListAdapter mOrderListAdapter;
    private FloatingActionButton mFloatingActionButton;
    private TabLayout mTabLayout;
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
        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout_main);
        mOrderListAdapter = new OrderListAdapter(getContext(), mOrderList);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_order_list);
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
            avQuery1.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.REPAY_PENDING);
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
        avQuery1.orderByDescending(OrderContract.OrderEntry.COLUMN_CREATE_AT);
        avQuery1.findInBackground(new FindCallback<AVObject>() {
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
}


