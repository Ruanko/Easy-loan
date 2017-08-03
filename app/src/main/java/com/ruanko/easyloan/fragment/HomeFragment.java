package com.ruanko.easyloan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.data.OrderContract;
import com.ruanko.easyloan.data.UserContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by deserts on 17/7/25.
 */

public class HomeFragment extends Fragment {
    private NestedScrollView mRootView;
    // arc related
    private DecoView mDecoView;
    private int mBackIndex;
    private TextView mTextPercentage;
    private TextView mTextBelowPercentage;
    private TextView mLoanCountTextView;
    private TextView mRecentRepayTextView;
    private TextView mOverdueTextView;

    private final float mSeriesMax = 100f;

    private ArrayList<Integer> COLORS;
    private ArrayList<Float> DATA;

    private float last_position;
    private boolean arc_first_flag = true;
    //    private RelativeLayout mRootView;
//    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mRootView =
                (NestedScrollView) inflater.inflate(R.layout.fragment_home, container, false);
        COLORS = new ArrayList<>();
        COLORS.add(ContextCompat.getColor(getContext(), R.color.colorAccent));
        COLORS.add(ContextCompat.getColor(getContext(), R.color.lime_primary));
        COLORS.add(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        loadData();
        initView();
        initDecoView(DATA);
        return this.mRootView;
    }

    private void initView() {
        mRecentRepayTextView = (TextView) mRootView.findViewById(R.id.tv_recent_repay);
        mLoanCountTextView = (TextView) mRootView.findViewById(R.id.tv_loan_count);
        mOverdueTextView = (TextView) mRootView.findViewById(R.id.tv_total_loan);
        AVQuery<AVObject> query3 = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);

        query3.whereEqualTo(OrderContract.OrderEntry.COLUMN_OWNER, AVUser.getCurrentUser());

        AVQuery<AVObject> query6 = new AVQuery<AVObject>(OrderContract.OrderEntry.TABLE_NAME);
        query6.whereEqualTo(OrderContract.OrderEntry.COLUMN_OWNER, AVUser.getCurrentUser());
        AVQuery<AVObject> query7 = AVQuery.and(Arrays.asList(query3, query6));
        query7.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null)
                    mLoanCountTextView.setText(String.valueOf(i));
            }
        });
        AVQuery<AVObject> query1 = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);
        AVQuery<AVObject> query2 = new AVQuery<>(OrderContract.OrderEntry.TABLE_NAME);
        query1.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.GRANT);
        query2.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.PARTIAL_REPAY);
        AVQuery<AVObject> query = AVQuery.or(Arrays.asList(query1, query2));
        AVQuery<AVObject> query4 = AVQuery.and(Arrays.asList(query3, query));
        query4.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null)
                    mRecentRepayTextView.setText(String.valueOf(i));
            }
        });
        AVQuery<AVObject> query8 = new AVQuery<AVObject>(OrderContract.OrderEntry.TABLE_NAME);
        query8.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.OVERDUE);
        AVQuery<AVObject> query5 = AVQuery.and(Arrays.asList(query3, query8));
        query5.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null)
                    mOverdueTextView.setText(String.valueOf(i));
            }
        });
    }

    private void loadData() {
        last_position = 0;
        DATA = new ArrayList<>();
        AVUser user = AVUser.getCurrentUser();
        int score1 = user.getInt(UserContract.UserEntry.COLUMN_LEVEL);
        int score2 = user.getInt(UserContract.UserEntry.COLUMN_INFO_LEVEL);
        int score3 = user.getInt(UserContract.UserEntry.COLUMN_TRADE_LEVEL);
        int data1 = score2 / 3;
        int data2 = score3 / 3;
        int data3 = score1 / 3;
        DATA.add((float) (data1 + data2 + data3));
        DATA.add((float) (data1 + data2));
        DATA.add((float) (data1));
    }

    private void initDecoView(final List<Float> data) {
        mDecoView = (DecoView) this.mRootView.findViewById(R.id.dynamicArcView);
        mTextPercentage = (TextView) this.mRootView.findViewById(R.id.tv_percentage);
        mTextBelowPercentage = (TextView) mRootView.findViewById(R.id.tv_below_percentage);
//        DecoView[] decoViews = {mDecoView, mDecoView2, mDecoView3};
        int duration;
        if (arc_first_flag) {
            duration = 1000;
        } else {
            duration = 0;
        }

        int delay = (data.size() - 1) * 2000 + 300;
        int delay_inner = 0;
        createBackSeries(mDecoView, 1000);
        for (int i = 0; i < data.size(); i++) {
            if (arc_first_flag)
                delay_inner = delay;
            if (i == data.size() - 1)
                createDataSeries(mDecoView, data.get(i), COLORS.get(i), delay_inner, duration, true);
            else
                createDataSeries(mDecoView, data.get(i), COLORS.get(i), delay_inner + 100, duration, false);
            delay -= 2000;
        }
        if (!arc_first_flag) {
            setText();
            return;
        }
        arc_first_flag = false;
        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(1)
                .setDelay(data.size() * 2000 + 1800)
                .setDuration(4000)
                .setDisplayText(getCreditLevel(data.get(0)))
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {
                        resetText();
                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        setText();
                        initDecoView(DATA);
                    }
                })
                .build());
    }

    private void createBackSeries(DecoView decoView, int duration) {
        SeriesItem seriesItem = new SeriesItem.Builder(ContextCompat.getColor(getContext(), R.color.arc_base_grey))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = decoView.addSeries(seriesItem);
        decoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(duration)
                .setDelay(0)
                .build());
    }

    private void createDataSeries(DecoView decoView, final float endAt, int color, int delay, int duration, boolean showStart) {
        final SeriesItem seriesItem = new SeriesItem.Builder(color)
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                if (currentPosition > last_position) {
                    mTextPercentage.setText(String.valueOf((int) currentPosition));
                    last_position = currentPosition;
                }
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        int mIndex = decoView.addSeries(seriesItem);

        if (showStart && !arc_first_flag) {
            decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                    .setIndex(mIndex)
                    .setDuration(duration)
                    .setEffectRotations(1)
                    .setDelay(delay)
                    .build());
        }
        decoView.addEvent(new DecoEvent.Builder(endAt)
                .setIndex(mIndex)
                .setDelay(delay + duration)
                .build());
    }

    private void resetText() {
        mTextBelowPercentage.setText("");
        mTextPercentage.setText("");
    }

    private String getCreditLevel(float score) {
        String string;
        if (score >= 90)
            string = getString(R.string.arc_final_text_level1);
        else if (score >= 80)
            string = getString(R.string.arc_final_text_level2);
        else if (score >= 70)
            string = getString(R.string.arc_final_text_level3);
        else
            string = getString(R.string.arc_final_text_level4);
        return string;
    }

    private void setText() {
        float score = DATA.get(0);
        mTextPercentage.setText(String.valueOf((int) score));
        mTextBelowPercentage.setText(getCreditLevel(score));
    }
}
