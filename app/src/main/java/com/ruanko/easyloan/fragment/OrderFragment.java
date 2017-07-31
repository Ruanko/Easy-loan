package com.ruanko.easyloan.fragment;

/**
 * Created by deserts on 17/7/25.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ruanko.easyloan.R;
import com.ruanko.easyloan.adapter.OrderFragmentPagerAdapter;

public class OrderFragment extends Fragment
{
    private RelativeLayout mRootView;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private RecyclerView mRecyclerView;
//    private List<AVObject> mOrderList = new ArrayList<>();
//    OrderListAdapter mOrderListAdapter;
    private FloatingActionButton mFloatingActionButton;
    private TabLayout mTabLayout;
    private ViewPager mViewPaper;

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
//        loadData();
        return mRootView;
    }

    private void initView() {
        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout_for_orders);
        mViewPaper = (ViewPager) mRootView.findViewById(R.id.view_pager_order);
        OrderFragmentPagerAdapter adapter = new OrderFragmentPagerAdapter(getChildFragmentManager());
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
        adapter.addFragment(fragment2, getString(R.string.tab_recent_order));
        adapter.addFragment(fragment3, getString(R.string.tab_unpassed_order));
        adapter.addFragment(fragment4, getString(R.string.tab_finished_order));
        adapter.addFragment(fragment5, getString(R.string.tab_overdue_order));
        mViewPaper.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPaper);
//        mOrderListAdapter = new OrderListAdapter(getContext(), mOrderList);
//        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.rv_order_list);
//        mFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_main_add_order);

//        if (getScreenWidthDp() >= 1200) {
//            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
//            mRecyclerView.setLayoutManager(gridLayoutManager);
//        } else if (getScreenWidthDp() >= 800) {
//            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
//            mRecyclerView.setLayoutManager(gridLayoutManager);
//        } else {
//            final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//            mRecyclerView.setLayoutManager(linearLayoutManager);
//        }

//        mRecyclerView.setAdapter(mOrderListAdapter);
//
//        mSwipeRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.swipe_refresh_layout_recycler_view);
//        mSwipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
//                R.color.google_green, R.color.google_red, R.color.google_yellow);
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
////                loadData();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                    }
//                }, 1000);
//
//            }
//        });

        // 滑动隐藏浮动按钮
//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState > 0) {
//                    mFloatingActionButton.hide();
//                } else {
//                    mFloatingActionButton.show();
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//            }
//        });

//        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivityForResult(new Intent(getContext(), ApplyActivity.class),
//                        ApplyActivity.APPLY_ACTIVITY_REQUEST_CODE);
//            }
//        });

    }


//    private int getScreenWidthDp() {
//        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//        return (int) (displayMetrics.widthPixels / displayMetrics.density);
//    }


//    private void loadData() {
//        AVQuery<AVObject> avQuery = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);
//        avQuery.orderByDescending("createdAt");
//        avQuery.whereEqualTo(OrderContract.OrderEntry.COLUMN_OWNER, AVUser.getCurrentUser());
//        avQuery.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (e == null) {
//                    mOrderListAdapter.updateData(list);
//                    mOrderListAdapter.notifyDataSetChanged();
//                } else {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ApplyActivity.APPLY_ACTIVITY_REQUEST_CODE
//                && resultCode == Activity.RESULT_OK) {
//            loadData();
//        }
//    }
}


