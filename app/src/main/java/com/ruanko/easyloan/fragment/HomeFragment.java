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
    private DecoView mDecoView1;
    private DecoView mDecoView2;
    private DecoView mDecoView3;
    private int mBackIndex;
    private TextView textPercentage;
    private TextView textBelowPercentage;
    private final float mSeriesMax = 100f;
    private boolean IS_FIRST_FLAG = true;

    private ArrayList<Integer> COLORS;
    private ArrayList<Float> DATA;


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
        DATA = new ArrayList<>();
        AVUser user = AVUser.getCurrentUser();
        int score1 = user.getInt(UserContract.UserEntry.COLUMN_LEVEL);
        int score2 = user.getInt(UserContract.UserEntry.COLUMN_INFO_LEVEL);
        int score3 = user.getInt(UserContract.UserEntry.COLUMN_TRADE_LEVEL);
        DATA.add((float)score1);
        DATA.add((float)score2);
        DATA.add((float)score3);
    }

    private void initDecoView(final List<Float> data) {
        mDecoView3 = (DecoView) this.rootView.findViewById(R.id.dynamicArcView1);
        mDecoView2 = (DecoView)this.rootView.findViewById(R.id.dynamicArcView2);
        mDecoView1 = (DecoView)this.rootView.findViewById(R.id.dynamicArcView3);
        DecoView[] decoViews = {mDecoView1, mDecoView2, mDecoView3};

        textPercentage = (TextView) this.rootView.findViewById(R.id.tv_percentage);
        textBelowPercentage = (TextView) rootView.findViewById(R.id.tv_below_percentage);
        int delay = 300;
        createBackSeries(mDecoView2);
        for (int i = 0; i < data.size(); i++) {
            //
            createDataSeries(decoViews[i], data.get(i), COLORS.get(i), delay);
            delay += 3000;
        }

        mDecoView3.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_EXPLODE)
                .setIndex(0)
                .setDelay(delay)
                .setDuration(4000)
                .setDisplayText(getCreditLevel(data.get(2)))
                .setListener(new DecoEvent.ExecuteEventListener() {
                    @Override
                    public void onEventStart(DecoEvent decoEvent) {
                        resetText();
                    }

                    @Override
                    public void onEventEnd(DecoEvent decoEvent) {
                        setText();
                        createDataSeries(mDecoView3, data.get(2), COLORS.get(2), 0);
                    }
                })
                .build());
    }

    private void createBackSeries(DecoView decoView) {
        SeriesItem seriesItem = new SeriesItem.Builder(ContextCompat.getColor(getContext(), R.color.arc_base_grey))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = decoView.addSeries(seriesItem);
        decoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(1000)
                .setDelay(0)
                .build());
    }

    private void createDataSeries(DecoView decoView, final float endAt, int color, int delay) {
        final SeriesItem seriesItem = new SeriesItem.Builder(color)
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();
        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textPercentage.setText(String.valueOf((int) currentPosition));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        int mIndex = decoView.addSeries(seriesItem);
        decoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mIndex)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(delay)
                .build());
        decoView.addEvent(new DecoEvent.Builder(endAt)
                .setIndex(mIndex)
                .setDelay(delay + 1000)
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
        float score = DATA.get(2);
        textPercentage.setText(String.valueOf((int)score));
        textBelowPercentage.setText(getCreditLevel(score));
    }
}
