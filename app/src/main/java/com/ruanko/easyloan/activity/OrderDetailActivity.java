package com.ruanko.easyloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.data.OrderContract;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

public class OrderDetailActivity extends AppCompatActivity {
    private ImageView topImageView;
    TextView titleTextView;
    TextView descriptionTextView;
    TextView amountTextView;
    TextView statusTextView;
    TextView repayMethodTextView;
    TextView grantAccountTextView;
    TextView deadlineTextView;
    TextView repaidTextView;
    TextView createDateTextView;
    TextView orderIdTextView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        loadData();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }

    private void initView() {
        titleTextView = (TextView) findViewById(R.id.tv_detail_title);
        descriptionTextView = (TextView) findViewById(R.id.tv_detail_description);
        amountTextView = (TextView) findViewById(R.id.tv_detail_amount);
        statusTextView = (TextView) findViewById(R.id.tv_detail_status);
        repayMethodTextView = (TextView) findViewById(R.id.tv_detail_repay_method);
        grantAccountTextView = (TextView) findViewById(R.id.tv_detail_grant_method);
        deadlineTextView = (TextView) findViewById(R.id.tv_detail_deadline);
        repaidTextView = (TextView) findViewById(R.id.tv_detail_repaid);
        topImageView = (ImageView) findViewById(R.id.image_order_detail);
        createDateTextView = (TextView) findViewById(R.id.tv_detail_create_date);
        orderIdTextView = (TextView) findViewById(R.id.tv_detail_id);
        Picasso.with(this).load(R.drawable.big_pic_cash).fit().into(topImageView);
        fab = (FloatingActionButton) findViewById(R.id.fab_order_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_order_detail);
        toolbar.setSubtitle("Shared Element Transitions");
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void loadData () {
        String orderId = getIntent().getStringExtra("orderObjectId");
        AVObject orderObject = AVObject.createWithoutData(OrderContract.OrderEntry.TABLE_NAME, orderId);
        orderObject.fetchInBackground("owner", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                afterLoadData(avObject);
            }
        });

    }

    private void afterLoadData(AVObject avObject){
        titleTextView.setText(avObject.getString(OrderContract.OrderEntry.COLUMN_TITLE));
        descriptionTextView.setText(avObject.getString(OrderContract.OrderEntry.COLUMN_DESCRIPTION));
        amountTextView.setText("￥" + String.valueOf((int) avObject.getInt(OrderContract.OrderEntry.COLUMN_AMOUNT)));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        deadlineTextView.setText(simpleDateFormat.format(avObject.getDate(OrderContract.OrderEntry.COLUMN_DEADLINE)));
        repaidTextView.setText(String.valueOf((int)avObject.getInt(OrderContract.OrderEntry.COLUMN_REPAID)));
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        createDateTextView.setText(simpleDateFormat.format(avObject.getCreatedAt()));
        orderIdTextView.setText(avObject.getObjectId());
        int methodGrant = avObject.getInt(OrderContract.OrderEntry.COLUMN_GRANT_METHOD);
        String[] grantMethods = getResources().getStringArray(R.array.type_received);
        grantAccountTextView.setText(grantMethods[methodGrant] + ": " +
                avObject.getString(OrderContract.OrderEntry.COLUMN_BANK_ACCOUNT));
        int methodRepay = avObject.getInt(OrderContract.OrderEntry.COLUMN_REPAY_METHOD);
        String[] repayMethods = getResources().getStringArray(R.array.by_stage_spinner);
        repayMethodTextView.setText(repayMethods[methodRepay]);
        int status = avObject.getInt(OrderContract.OrderEntry.COLUMN_STATUS);
        String[] statusArray = getResources().getStringArray(R.array.status);
        statusTextView.setText(statusArray[status]);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,
                        String.format("我在软酷E贷成功申请借款%s元，你要不要来!\nhttps://panjunwen.com", amountTextView.getText()));
                intent.setType("text/plain");
                startActivity(intent);
            }
        });
    }
}
