package com.ruanko.easyloan.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.ruanko.easyloan.R;
import com.ruanko.easyloan.data.OrderContract;

import java.util.Calendar;
import java.util.Date;

public class ApplyActivity extends AppCompatActivity {
    EditText amountEt;
    EditText titleEt;
    Spinner repayMethodSp;
    Spinner payMethodSp;
    EditText bankAccountEt;
    EditText moreInfoEt;
    ProgressBar progressBar;
    NestedScrollView scrollView;
    Button deadlineBtn;
    Calendar calendar;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        calendar = Calendar.getInstance();
        initView();
    }

    private void initView() {
        scrollView = (NestedScrollView) findViewById(R.id.ns_apply);
        progressBar = (ProgressBar) findViewById(R.id.progress_apply);
        amountEt = (EditText) findViewById(R.id.et_amount);
        amountEt.requestFocus();
        titleEt = (EditText) findViewById(R.id.et_order_title);
        repayMethodSp = (Spinner) findViewById(R.id.sp_by_stage);
        payMethodSp = (Spinner) findViewById(R.id.sp_pay_method);
        bankAccountEt = (EditText) findViewById(R.id.et_bank_account);
        moreInfoEt = (EditText) findViewById(R.id.et_more_info);
        deadlineBtn = (Button) findViewById(R.id.btn_select_deadline);
        deadlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ApplyActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        deadlineBtn.setText(date);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    private boolean isInfoValid() {
        if ("".equals(amountEt.getText().toString())) {
            Toast.makeText(ApplyActivity.this, "请输入金额", Toast.LENGTH_SHORT).show();
            amountEt.requestFocus();
            return false;
        }
        if ("".equals(titleEt.getText().toString())) {
            Toast.makeText(ApplyActivity.this, "请输入借款事项", Toast.LENGTH_SHORT).show();
            titleEt.requestFocus();
            return false;
        }
        if ("".equals(bankAccountEt.getText().toString())) {
            Toast.makeText(ApplyActivity.this, "请输入账户", Toast.LENGTH_SHORT).show();
            bankAccountEt.requestFocus();
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu_apply, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_apply:
                attemptApply();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void attemptApply(){
        if (!isInfoValid()) {
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        hideInput(amountEt);
        AVObject order = new AVObject(OrderContract.OrderEntry.TABLE_NAME);
        order.put(OrderContract.OrderEntry.COLUMN_TITLE, titleEt.getText().toString());
        order.put(OrderContract.OrderEntry.COLUMN_DESCRIPTION, moreInfoEt.getText().toString());
        order.put(OrderContract.OrderEntry.COLUMN_AMOUNT, Integer.parseInt(amountEt.getText().toString()));
        order.put(OrderContract.OrderEntry.COLUMN_REPAY_METHOD, repayMethodSp.getSelectedItemPosition());
        order.put(OrderContract.OrderEntry.COLUMN_GRANT_METHOD, payMethodSp.getSelectedItemPosition());
        order.put(OrderContract.OrderEntry.COLUMN_BANK_ACCOUNT, bankAccountEt.getText().toString());
        order.put(OrderContract.OrderEntry.COLUMN_REPAID, 0);
        order.put(OrderContract.OrderEntry.COLUMN_STATUS, 0);
        order.put(OrderContract.OrderEntry.COLUMN_OWNER, AVUser.getCurrentUser());
        Date deadline = calendar.getTime();
        order.put(OrderContract.OrderEntry.COLUMN_DEADLINE, deadline);
        order.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.INVISIBLE);
                if (e == null) {
                    Toast.makeText(ApplyActivity.this,
                            getString(R.string.apply_done),
                            Toast.LENGTH_LONG).show();
                    ApplyActivity.this.finish();
                } else {
                    scrollView.setVisibility(View.VISIBLE);
                    Toast.makeText(ApplyActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void hideInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
