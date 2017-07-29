package com.ruanko.easyloan.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.avos.avoscloud.FindCallback;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.adapter.OrderListAdapter;
import com.ruanko.easyloan.data.OrderContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deserts on 17/7/25.
 */

public class OrderFragment extends Fragment
{
    private RelativeLayout rootView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<AVObject> orderList = new ArrayList<>();
    OrderListAdapter orderListAdapter;
    private int color = 0;
//    private FloatingActionButton floatingActionButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = (RelativeLayout) inflater.inflate(
                R.layout.fragment_order, container, false);
        initView();
        initData();
        return rootView;
    }

    private void initView() {
//        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab_add_order);
        orderListAdapter = new OrderListAdapter(getContext(), orderList);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_order_list);

        if (getScreenWidthDp() >= 1200) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
            mRecyclerView.setLayoutManager(gridLayoutManager);
//            floatingActionButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    orderListAdapter.addItem(gridLayoutManager.findFirstVisibleItemPosition() + 1);
//                }
//            });
        } else if (getScreenWidthDp() >= 800) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            mRecyclerView.setLayoutManager(gridLayoutManager);
//            floatingActionButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    orderListAdapter.addItem(gridLayoutManager.findFirstVisibleItemPosition() + 1);
//                }
//            });
        } else {
            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(linearLayoutManager);
//            floatingActionButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    orderListAdapter.addItem(linearLayoutManager.findFirstVisibleItemPosition() + 1);
//                }
//            });
        }

        mRecyclerView.setAdapter(orderListAdapter);

        //关联ItemTouchHelper和RecyclerView
//        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(orderListAdapter);
//        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
//        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout_recycler_view);
        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green, R.color.google_red, R.color.google_yellow);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 1000);

            }
        });

        // 滑动隐藏浮动按钮
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
//                if (newState > 0) {
//                    floatingActionButton.hide();
//                } else {
//                    floatingActionButton.show();
//                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }


    private int getScreenWidthDp() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (displayMetrics.widthPixels / displayMetrics.density);
    }


    private void initData() {
        orderList.clear();
        AVQuery<AVObject> avQuery = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);
        avQuery.orderByDescending("createdAt");
        avQuery.include("owner");
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    orderList.addAll(list);
                    orderListAdapter.notifyDataSetChanged();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

}


