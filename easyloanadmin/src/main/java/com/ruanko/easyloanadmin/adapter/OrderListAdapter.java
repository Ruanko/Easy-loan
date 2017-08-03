/**
 * Created by deserts on 17/7/25.
 */

package com.ruanko.easyloanadmin.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.avos.avoscloud.AVObject;
import com.ruanko.easyloanadmin.R;
import com.ruanko.easyloanadmin.activity.OrderDetailActivity;
import com.ruanko.easyloanadmin.data.OrderContract;
import com.ruanko.easyloanadmin.utilities.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    private Context context;
    private List<AVObject> mOrderList;
    private static ColorGenerator COLOR_GENERATOR = ColorGenerator.MATERIAL;
    public static List<Integer> COLORS;

    public OrderListAdapter(Context context, List<AVObject> orders) {
        this.context = context;
        this.mOrderList = orders;
        COLORS = new ArrayList<Integer>();
        COLORS.add(context.getResources().getColor(R.color.colorPrimary));
        COLORS.add(context.getResources().getColor(R.color.colorAccent));
        COLORS.add(context.getResources().getColor(R.color.lime_primary));
//        COLOR_GENERATOR = ColorGenerator.create(colors);
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);
        return new OrderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderListViewHolder holder, final int position) {
        AVObject orderObject = mOrderList.get(position);
        String title = orderObject.getString(OrderContract.OrderEntry.COLUMN_TITLE);
        holder.titleText.setText(title);
        int amount = orderObject.getInt(OrderContract.OrderEntry.COLUMN_AMOUNT);
        holder.amountText.setText("金额：￥" + amount);
        Date date = orderObject.getDate(OrderContract.OrderEntry.COLUMN_DEADLINE);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        holder.dateText.setText("最后还款日期：" + simpleDateFormat.format(date));
//        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler_item_show);
//        holder.mView.startAnimation(animation);
//        holder.initRoundIcon(String.valueOf(title.charAt(0)));
        int status = orderObject.getInt(OrderContract.OrderEntry.COLUMN_STATUS);
        if (status == OrderContract.Status.GRANT
                || status == OrderContract.Status.PARTIAL_REPAY)
            holder.initRoundIcon(String.valueOf(title.charAt(0)), COLORS.get(1));
        else if (status == OrderContract.Status.DONE)
            holder.initRoundIcon(String.valueOf(title.charAt(0)), COLORS.get(2));
        else
            holder.initRoundIcon(String.valueOf(title.charAt(0)), COLORS.get(0));

//        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
//        aa1.setDuration(400);
//        holder.roundIcon.startAnimation(aa1);
//
//        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
//        aa.setDuration(400);
//
//        holder.roundIcon.startAnimation(aa);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击进入详情
                Intent intent = new Intent(context, OrderDetailActivity.class);
                intent.putExtra("orderObjectId", mOrderList.get(position).getObjectId());
                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
                        ((Activity) context, holder.roundIcon, "orderDetail").toBundle());
            }
        });

        //08/01 update order status
//        Date deadline = orderObject.getDate(OrderContract.OrderEntry.COLUMN_DEADLINE);
        if (DateUtils.differentDays(new Date(), date) < 0) {
//            int status = orderObject.getInt(OrderContract.OrderEntry.COLUMN_STATUS);
            if (status < OrderContract.Status.GRANT) {
                orderObject.deleteInBackground();
            } else if (status >= OrderContract.Status.GRANT && status < OrderContract.Status.OVERDUE) {
                orderObject.put(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.OVERDUE);
                orderObject.saveInBackground();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    public void updateData(List<AVObject> orderList) {
        if (orderList != null) {
            int previousSize = orderList.size();
            mOrderList.clear();
            notifyItemRangeRemoved(0, previousSize);
            mOrderList.addAll(orderList);
            notifyItemRangeInserted(0, orderList.size());
        }
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

        private void initRoundIcon(String letter, int color) {
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, color);
            roundIcon.setImageDrawable(drawable);
        }
    }

}
