/**
 * Created by deserts on 17/7/25.
 */
package com.ruanko.easyloan.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.ruanko.easyloan.R;
import com.wx.ovalimageview.RoundImageView;

public class AccountFragment extends Fragment
{
    private NestedScrollView rootView;
    private RoundImageView userHeadImage;
    private TextView nameTextView;
    private TextView infoTextView;
    private TextView realNameTextView;
    private TextView phoneTextView;
    private TextView schoolTextView;
    private TextView mailTextView;

    private TextView levelTextView;
    private TextView infoIntegrityTextView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.rootView = (NestedScrollView) inflater.inflate(R.layout.fragment_account, container, false);
        initView();
        return rootView;
    }

    private void initView () {
        userHeadImage = (RoundImageView) rootView.findViewById(R.id.img_user_head);
        nameTextView = (TextView) rootView.findViewById(R.id.tv_real_name);
        infoTextView = (TextView) rootView.findViewById(R.id.tv_account_mail);
        realNameTextView = (TextView) rootView.findViewById(R.id.tv_name_account_center);
        phoneTextView = (TextView) rootView.findViewById(R.id.tv_phone_account_center);
        schoolTextView = (TextView) rootView.findViewById(R.id.tv_school_account_center);
        mailTextView = (TextView) rootView.findViewById(R.id.tv_mail_account_center);

        levelTextView = (TextView) rootView.findViewById(R.id.tv_review_account_center);
        infoIntegrityTextView = (TextView) rootView.findViewById(R.id.tv_info_account_center);

        AVUser user = AVUser.getCurrentUser();
        if (user != null) {
            nameTextView.setText(user.getUsername());
            infoTextView.setText(user.getMobilePhoneNumber());
            if (user.getMobilePhoneNumber() != null) {
                phoneTextView.setText(user.getMobilePhoneNumber());
            }
            if (user.getEmail()!=null){
                mailTextView.setText(user.getEmail());
            }
            if (user.getString("school") != null) {
                schoolTextView.setText(user.getString("school"));
            }
            if (user.getString("real_name") != null) {
                realNameTextView.setText(user.getString("real_name"));
            }
        }
        else {
            resetView();
        }
    }

    private void resetView() {
        nameTextView.setText("");
        infoTextView.setText("");
        realNameTextView.setText("");
        phoneTextView.setText("");
        schoolTextView.setText("");
        mailTextView.setText("");
        levelTextView.setText("");
        infoIntegrityTextView.setText("");
    }
}
