package com.ruanko.easyloanadmin.fragment;

/**
 * Created by deserts on 17/7/25.
 */
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ruanko.easyloanadmin.R;
import com.ruanko.easyloanadmin.adapter.OrderFragmentPagerAdapter;

public class OrderFragment extends Fragment
{
    private RelativeLayout mRootView;
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
        return mRootView;
    }

    private void initView() {
        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout_main);
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
        adapter.addFragment(fragment3, getString(R.string.tab_apply_check));
        adapter.addFragment(fragment2, getString(R.string.tab_repay_request));
        adapter.addFragment(fragment4, getString(R.string.tab_finished_order));
        adapter.addFragment(fragment5, getString(R.string.tab_overdue_order));
        mViewPaper.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPaper);
    }

}


