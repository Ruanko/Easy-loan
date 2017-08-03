package com.ruanko.easyloanadmin.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.hookedonplay.decoviewlib.DecoView;
import com.ruanko.easyloanadmin.R;
import com.ruanko.easyloanadmin.data.OrderContract;
import com.ruanko.easyloanadmin.data.SettingsContract;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ruanko.easyloanadmin.utilities.FileUtils.getFileBytes;

/**
 * Created by deserts on 17/7/25.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private NestedScrollView mRootView;
    // arc related
    private DecoView mDecoView;
    private Banner banner;
    private int mBackIndex;
    private TextView mTextPercentage;
    private TextView mTextBelowPercentage;
    private TextView mLoanCountTextView;
    private TextView mRecentRepayTextView;
    private TextView mOverdueTextView;
    Context context;
    ImageView previewImageView;
    Button selectPicButton;
    EditText mBannerTitleEditText;

    private byte[] mImageBytes = null;
    //    private RelativeLayout mRootView;
//    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mRootView =
                (NestedScrollView) inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
//        loadData();
        initView();
//        initDecoView(DATA);
        return this.mRootView;
    }

    private void initView() {
        mRecentRepayTextView = (TextView) mRootView.findViewById(R.id.tv_recent_repay);
        mLoanCountTextView = (TextView) mRootView.findViewById(R.id.tv_loan_count);
        mOverdueTextView = (TextView) mRootView.findViewById(R.id.tv_total_loan);

        AVQuery<AVObject> query6 = new AVQuery<AVObject>(OrderContract.OrderEntry.TABLE_NAME);
        query6.countInBackground(new CountCallback() {
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
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null)
                    mRecentRepayTextView.setText(String.valueOf(i));
            }
        });
        AVQuery<AVObject> query8 = new AVQuery<AVObject>(OrderContract.OrderEntry.TABLE_NAME);
        query8.whereEqualTo(OrderContract.OrderEntry.COLUMN_STATUS, OrderContract.Status.OVERDUE);
        query8.countInBackground(new CountCallback() {
            @Override
            public void done(int i, AVException e) {
                if (e == null)
                    mOverdueTextView.setText(String.valueOf(i));
            }
        });
        banner = (Banner) mRootView.findViewById(R.id.banner);
        Button updateButton = (Button) mRootView.findViewById(R.id.btn_update_banner);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
                final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_upload, null);
                Button btn_dialog_bottom_sheet_ok = (Button) dialogView.findViewById(R.id.btn_dialog_bottom_sheet_ok);
                mBannerTitleEditText = (EditText) dialogView.findViewById(R.id.et_banner_title);
//                Button btn_dialog_bottom_sheet_cancel = (Button) dialogView.findViewById(R.id.btn_dialog_bottom_sheet_cancel);
//                ImageView img_bottom_dialog = (ImageView) dialogView.findViewById(R.id.img_bottom_dialog);
//                Picasso.with(getContext()).load(getActivity().getString(R.string.wechat_qrcode_url)).into(img_bottom_dialog);
                bottomSheetDialog.setContentView(dialogView);
                previewImageView = (ImageView) dialogView.findViewById(R.id.img_prepare_upload);
                selectPicButton = (Button) dialogView.findViewById(R.id.btn_select_pic);
                selectPicButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setActivityTitle(getString(R.string.crop_image_title))
                                .setOutputCompressQuality(90)
                                .setOutputCompressFormat(Bitmap.CompressFormat.JPEG)
                                .start(context, HomeFragment.this);
                    }
                });
                btn_dialog_bottom_sheet_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
            }
        });
        banner.setVisibility(View.VISIBLE);
        //设置图片加载器

        loadBanner();
    }


    private void loadBanner() {
        //设置图片集合
        banner.setImageLoader(new HomeFragment.GlideImageLoader());
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        final ArrayList images = new ArrayList<String>();
        final ArrayList<String> titles = new ArrayList<>();
        AVQuery<AVObject> avQuery = new AVQuery<>(SettingsContract.BannerEntry.TABLE_NAME);
        avQuery.orderByAscending(SettingsContract.BannerEntry.COLUMN_CREATE_AT);
        avQuery.limit(3);
        avQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                int count = 5;
                for (AVObject object : list){
                    AVFile imageFile = object.getAVFile(SettingsContract.BannerEntry.COLUMN_IMAGE);
                    if (imageFile != null) {
                        Log.d("", "Home fragment: get url = " + imageFile.getUrl());
                        images.add(imageFile.getUrl());
                        titles.add(object.getString(SettingsContract.BannerEntry.COLUMN_TITLE));
                    }
                }
                banner.setImages(images);
                banner.setBannerTitles(titles);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Picasso.with(context).load((String) path).into(imageView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == Activity.RESULT_OK) {
            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
            try {
                mImageBytes = getFileBytes(context.getContentResolver()
                        .openInputStream(result.getUri()));
                AVObject avObject = new AVObject(SettingsContract.BannerEntry.TABLE_NAME);
                avObject.put(SettingsContract.BannerEntry.COLUMN_TITLE, mBannerTitleEditText.getText().toString());
                avObject.put(SettingsContract.BannerEntry.COLUMN_IMAGE, new AVFile("banner.jpg", mImageBytes));
                avObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            Picasso.with(getContext()).load(result.getUri()).into(previewImageView);
                            loadBanner();
                            selectPicButton.setText("上传完成，点击继续选择图片");
                            Toast.makeText(context, getString(R.string.upload_done), Toast.LENGTH_LONG).show();
                        } else {
                            String json = e.getMessage();
                            JSONTokener tokener = new JSONTokener(json);
                            try {
                                JSONObject jsonObject = (JSONObject) tokener.nextValue();
                                Toast.makeText(HomeFragment.this.getContext(),
                                        jsonObject.getString("error"),
                                        Toast.LENGTH_LONG).show();
                            } catch (JSONException jse) {
                                jse.printStackTrace();
                            }
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
