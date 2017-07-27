/**
 * Created by deserts on 17/7/25.
 */

package com.ruanko.easyloan.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.ruanko.easyloan.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder> {

    private Context context;
    private List mItems;
    private int color = 0;

    public OrderListAdapter(Context context) {
        this.context = context;
        mItems = new ArrayList();
        mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.recycler_name_array)));
    }

    public void setItems(int color) {
        this.color = color;
        notifyDataSetChanged();
    }

    public void addItem(int position) {
        mItems.add(position);
        notifyItemInserted(position);
    }

    public void removeItem(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);
        return new OrderListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final OrderListViewHolder holder, int position) {
        holder.position = position;

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.anim_recycler_item_show);
        holder.mView.startAnimation(animation);

        AlphaAnimation aa1 = new AlphaAnimation(1.0f, 0.1f);
        aa1.setDuration(400);
        holder.roundIcon.startAnimation(aa1);

        AlphaAnimation aa = new AlphaAnimation(0.1f, 1.0f);
        aa.setDuration(400);

        holder.roundIcon.startAnimation(aa);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 待完成，点击进入详情
//                Intent intent = new Intent(context, ShareViewActivity.class);
//                intent.putExtra("color", color);
//                context.startActivity(intent, ActivityOptions.makeSceneTransitionAnimation
//                        ((Activity) context, holder.round, "shareView").toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class OrderListViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private int position;
        private ImageView roundIcon;

        private OrderListViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            roundIcon = (ImageView) itemView.findViewById(R.id.text_icon_order_item);
            initRoundIcon();
        }

        private void initRoundIcon() {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound("潘", generator.getRandomColor());
            roundIcon.setImageDrawable(drawable);
        }
    }

}
