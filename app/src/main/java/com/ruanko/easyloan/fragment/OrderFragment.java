package com.ruanko.easyloan.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.activity.ApplyActivity;
import com.ruanko.easyloan.adapter.OrderListAdapter;
import com.ruanko.easyloan.data.OrderContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deserts on 17/7/25.
 */

public class OrderFragment extends Fragment
{
    private RelativeLayout mRootView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<AVObject> mOrderList = new ArrayList<>();
    OrderListAdapter mOrderListAdapter;
    private FloatingActionButton mFloatingActionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mRootView = (RelativeLayout) inflater.inflate(
                R.layout.fragment_order, container, false);
        initView();
        loadData();
        return mRootView;
    }

    private void initView() {
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

        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout_recycler_view);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green, R.color.google_red, R.color.google_yellow);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);

            }
        });

        // 滑动隐藏浮动按钮
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState > 0) {
                    mFloatingActionButton.hide();
                } else {
                    mFloatingActionButton.show();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
        mOrderList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);
        avQuery.orderByDescending("createdAt");
        avQuery.whereEqualTo(OrderContract.OrderEntry.COLUMN_OWNER, AVUser.getCurrentUser());
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    mOrderList.addAll(list);
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
}


