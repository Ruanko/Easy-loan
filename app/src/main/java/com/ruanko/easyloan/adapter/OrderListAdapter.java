/**
 * Created by deserts on 17/7/25.
 */

package com.ruanko.easyloan.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.avos.avoscloud.AVObject;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.activity.OrderDetailActivity;
import com.ruanko.easyloan.data.OrderContract;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    private Context context;
    private List<AVObject> orderList;

    public OrderListAdapter(Context context, List<AVObject> orders) {
        this.context = context;
        this.orderList = orders;
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);
        return new OrderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderListViewHolder holder, final int position) {
        AVObject orderObject = orderList.get(position);
        String title = orderObject.getString(OrderContract.OrderEntry.COLUMN_TITLE);
        holder.titleText.setText(title);
        int amount = orderObject.getInt(OrderContract.OrderEntry.COLUMN_AMOUNT);
        holder.amountText.setText("金额：￥" + amount);
        Date date = orderObject.getDate(OrderContract.OrderEntry.COLUMN_DEADLINE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.dateText.setText("最后还款日期：" + simpleDateFormat.format(date));
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler_item_show);
        holder.mView.startAnimation(animation);
        holder.initRoundIcon(String.valueOf(title.charAt(0)));

        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
        aa1.setDuration(400);
        holder.roundIcon.startAnimation(aa1);

        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(400);

        holder.roundIcon.startAnimation(aa);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击进入详情
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderObjectId", orderList.get(position).getObjectId());
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
                        ((Activity) context, holder.roundIcon, "orderDetail").toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderListViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private ImageView roundIcon;
        private TextView titleText;
        private TextView dateText;
        private TextView amountText;

        private OrderListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            roundIcon = (ImageView) mView.findViewById(R.id.text_icon_order_item);
            titleText = (TextView) mView.findViewById(R.id.tv_order_item_title);
            dateText = (TextView) mView.findViewById(R.id.tv_order_item_date);
            amountText = (TextView) mView.findViewById(R.id.tv_order_item_amount);
        }

        private void initRoundIcon(String letter) {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            roundIcon.setImageDrawable(drawable);
        }
    }

}
