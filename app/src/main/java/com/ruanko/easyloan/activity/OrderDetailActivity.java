package com.ruanko.easyloan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.GetCallback;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.data.OrderContract;
import com.ruanko.easyloan.utilities.DateUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import moe.feng.alipay.zerosdk.AlipayZeroSdk;

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
    TextView shouldRepayTextView;
    FloatingActionButton fab;

    Button repayButton;
    Button deleteButton;
    Button undoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initView();
        loadData();
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

    }

    private void initView() {
        shouldRepayTextView = (TextView) findViewById(R.id.tv_detail_should_repay);
        repayButton = (Button) findViewById(R.id.btn_repay);
        deleteButton = (Button) findViewById(R.id.btn_delete);
        undoButton = (Button) findViewById(R.id.btn_undo_apply);
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

    private void loadData() {
        String orderId = getIntent().getStringExtra("orderObjectId");
        AVObject orderObject = AVObject.createWithoutData(OrderContract.OrderEntry.TABLE_NAME, orderId);
        orderObject.fetchInBackground("owner", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                afterLoadData(avObject);
            }
        });

    }

    private void afterLoadData(final AVObject avObject) {
        titleTextView.setText(avObject.getString(OrderContract.OrderEntry.COLUMN_TITLE));
        descriptionTextView.setText(avObject.getString(OrderContract.OrderEntry.COLUMN_DESCRIPTION));
        amountTextView.setText(
                "￥" + String.valueOf((int) avObject.getInt(OrderContract.OrderEntry.COLUMN_AMOUNT)));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        deadlineTextView.setText(
                simpleDateFormat.format(avObject.getDate(OrderContract.OrderEntry.COLUMN_DEADLINE)));
        repaidTextView.setText(String.valueOf((int) avObject.getInt(OrderContract.OrderEntry.COLUMN_REPAID)));

        int amount = avObject.getInt(OrderContract.OrderEntry.COLUMN_AMOUNT);
        int days = DateUtils.differentDays(avObject.getCreatedAt(), avObject.getDate(OrderContract.OrderEntry.COLUMN_DEADLINE));
//        int months = days / 30 + 1;
        double yearProfit = 0.0435;
        double shouldRepay = (days * amount * (yearProfit / 12.0 / 30.0)) + amount;
        shouldRepayTextView.setText(String.format("￥%.2f", shouldRepay));
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
        if (status < OrderContract.Status.GRANT) {
            undoButton.setVisibility(View.VISIBLE);
            repayButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        } else if (status < OrderContract.Status.DONE) {
            undoButton.setVisibility(View.GONE);
            repayButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
        } else {
            undoButton.setVisibility(View.GONE);
            repayButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);
        }

        //设置按钮
        repayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(OrderDetailActivity.this);
                View dialogView = OrderDetailActivity.this.getLayoutInflater().inflate(R.layout.dialog_bottom_sheet, null);
                Button btn_dialog_bottom_sheet_ok = (Button) dialogView.findViewById(R.id.btn_dialog_bottom_sheet_ok);
                Button btn_dialog_bottom_sheet_cancel = (Button) dialogView.findViewById(R.id.btn_dialog_bottom_sheet_cancel);
                ImageView img_bottom_dialog = (ImageView) dialogView.findViewById(R.id.img_bottom_dialog);
                Picasso.with(OrderDetailActivity.this).load(OrderDetailActivity.this.getString(R.string.wechat_qrcode_url)).into(img_bottom_dialog);
                mBottomSheetDialog.setContentView(dialogView);

                btn_dialog_bottom_sheet_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (AlipayZeroSdk.hasInstalledAlipayClient(OrderDetailActivity.this)) {
                            AlipayZeroSdk.startAlipayClient(OrderDetailActivity.this,
                                    OrderDetailActivity.this.getString(R.string.alipay_account));
                        }
                        else {
                            Snackbar.make(v, getString(R.string.alipay_not_installed), Snackbar.LENGTH_LONG)
                                    .show();
                        }
                        mBottomSheetDialog.dismiss();
                    }
                });
                btn_dialog_bottom_sheet_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBottomSheetDialog.dismiss();
                    }
                });
                mBottomSheetDialog.show();
            }
        });
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, getString(R.string.snack_bar_undo_order), Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_undo_apply, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                avObject.deleteInBackground();
                                onBackPressed();
                            }
                        })
                        .show();
            }
        });
    }



}
