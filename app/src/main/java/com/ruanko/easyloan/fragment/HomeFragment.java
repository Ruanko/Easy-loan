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

import com.avos.avoscloud.AVUser;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.data.UserContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deserts on 17/7/25.
 */

public class HomeFragment extends Fragment {
    private NestedScrollView rootView;
    // arc related
    private DecoView mDecoView;
//    private DecoView mDecoView2;
//    private DecoView mDecoView3;
    private int mBackIndex;
    private TextView textPercentage;
    private TextView textBelowPercentage;
    private final float mSeriesMax = 100f;
    private boolean IS_FIRST_FLAG = true;

    private ArrayList<Integer> COLORS;
    private ArrayList<Float> DATA;

    private float last_position;
    private boolean arc_first_flag = true;
    //    private RelativeLayout rootView;
//    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView =
                (NestedScrollView) inflater.inflate(R.layout.fragment_home, container, false);
        COLORS = new ArrayList<>();
        COLORS.add(ContextCompat.getColor(getContext(), R.color.google_blue));
        COLORS.add(ContextCompat.getColor(getContext(), R.color.google_yellow));
        COLORS.add(ContextCompat.getColor(getContext(), R.color.google_red));
        loadData();

        initDecoView(DATA);
        return this.rootView;
    }

    private void loadData(){
        last_position = 0;
        DATA = new ArrayList<>();
        AVUser user = AVUser.getCurrentUser();
        int score1 = user.getInt(UserContract.UserEntry.COLUMN_LEVEL);
        int score2 = user.getInt(UserContract.UserEntry.COLUMN_INFO_LEVEL);
        int score3 = user.getInt(UserContract.UserEntry.COLUMN_TRADE_LEVEL);
        int data1 = score2 / 3;
        int data2 = score3 / 3;
        int data3 = score1 / 3;
        DATA.add((float)(data1 + data2 + data3));
        DATA.add((float)(data1 + data2));
        DATA.add((float)(data1));
//        DATA.add(60f);
//        DATA.add(40f);
//        DATA.add(20f);
    }

    private void initDecoView(final List<Float> data) {
        mDecoView = (DecoView)this.rootView.findViewById(R.id.dynamicArcView);
        textPercentage = (TextView) this.rootView.findViewById(R.id.tv_percentage);
        textBelowPercentage = (TextView) rootView.findViewById(R.id.tv_below_percentage);
//        DecoView[] decoViews = {mDecoView, mDecoView2, mDecoView3};
        int duration;
        if (arc_first_flag) {
            duration = 1000;
        }
        else {
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
                createDataSeries(mDecoView, data.get(i), COLORS.get(i), delay_inner+100, duration, false);
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
                    textPercentage.setText(String.valueOf((int) currentPosition));
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

    private void resetText () {
        textBelowPercentage.setText("");
        textPercentage.setText("");
    }

    private String getCreditLevel (float score) {
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

    private void setText () {
        float score = DATA.get(0) ;
        textPercentage.setText(String.valueOf((int)score));
        textBelowPercentage.setText(getCreditLevel(score));
    }
}
