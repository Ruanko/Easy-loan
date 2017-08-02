package com.ruanko.easyloanadmin.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.ruanko.easyloanadmin.R;
import com.ruanko.easyloanadmin.data.UserContract;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class UserInfoActivity extends AppCompatActivity {
    public static final int USER_INFO_ACTIVITY_REQUEST_CODE = 10012;
    private String[] province;
    private String[] school;
    private Spinner mProvinceSpinner;
    private Spinner mSchoolSpinner;
    private EditText mRealNameEditText;
    private EditText mIdCardEditText;
    private EditText mAgeEditText;
    private EditText mPhoneEditText;
    private EditText mRelativeNameEditText;
    private EditText mRelativePhoneEditText;
    private EditText mEmailEditText;
    private EditText mHomeAddressEditText;
    private Spinner mSexSpinner;
    private Spinner mCardTypeSpinner;
    private Spinner mRelationSpinner;

    private Context context;
    ArrayAdapter<String> mSchoolAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        context = this;
        initView();
        loadData();
    }

    private void initView() {
        mHomeAddressEditText = (EditText) findViewById(R.id.u_home_address);
        mEmailEditText = (EditText) findViewById(R.id.u_mail_address);
        mRealNameEditText = (EditText)findViewById(R.id.u_real_name);
        mIdCardEditText = (EditText)findViewById(R.id.u_card_id);
        mPhoneEditText = (EditText) findViewById(R.id.u_phone_number);
        mRelativeNameEditText = (EditText)findViewById(R.id.u_relative_real_name);
        mRelativePhoneEditText = (EditText)findViewById(R.id.u_relative_phone_number);
        mSexSpinner = (Spinner)findViewById(R.id.u_sp_sex_person_select);
        mCardTypeSpinner = (Spinner)findViewById(R.id.u_type_card);
        mRelationSpinner = (Spinner)findViewById(R.id.u_relative_relation);
        mAgeEditText = (EditText) findViewById(R.id.u_age);

        //
        province = this.getResources().getStringArray(R.array.province);
        mSchoolSpinner =(Spinner)findViewById(R.id.u_school_select);
        mProvinceSpinner =(Spinner)findViewById(R.id.u_province_select);
        mProvinceSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView parent, View v, int position, long id){
                int pos = mProvinceSpinner.getSelectedItemPosition();
                school=getSchool(pos);
                mSchoolAdapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, school);//school[pos]
                mSchoolAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mSchoolSpinner.setAdapter(mSchoolAdapter);
            }
            public void onNothingSelected(AdapterView arg0){
            }
        });
    }

    private String[] getSchool(int index){
        String[] sh=null;
        switch (index){
            case 0:
                sh = this.getResources().getStringArray(R.array.school0);
                break;
            case 1:
                sh = this.getResources().getStringArray(R.array.school1);
                break;
            case 2:
                sh = this.getResources().getStringArray(R.array.school2);
                break;
            case 3:
                sh = this.getResources().getStringArray(R.array.school3);
                break;
            case 4:
                sh = this.getResources().getStringArray(R.array.school4);
                break;
            default:
                break;
        }
        return sh;
    }
    /**
     * 按设定规则解析一维数组为二维数组
     * @param array
     * @return
     */
    private String[][] getTwoDimensionalArray(String[] array) {
        String[][] ssStrings = null;
        for (int i = 0; i < array.length; i++) {
            String[] bbStrings = array[i].split(",");
            if (ssStrings == null) {
                ssStrings = new String[array.length][3];
            }
            for (int j = 0; j < bbStrings.length; j++) {
                System.out.println("bbStrings[j]=" + bbStrings[j]);
                ssStrings[i][j] = bbStrings[j];
            }
        }
        return ssStrings;
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
                attemptModify();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData () {
        AVUser user = AVUser.getCurrentUser();
        if (user == null)
            return;
        mRealNameEditText.setText(user.getString(UserContract.UserEntry.COLUMN_REAL_NAME));
        mAgeEditText.setText(String.valueOf(user.getInt(UserContract.UserEntry.COLUMN_AGE)));
        mIdCardEditText.setText(user.getString(UserContract.UserEntry.COLUMN_ID_CARD));
        mPhoneEditText.setText(user.getMobilePhoneNumber());
        mRelativeNameEditText.setText(user.getString(UserContract.UserEntry.COLUMN_RELATIVE_NAME));
        mRelativePhoneEditText.setText(user.getString(UserContract.UserEntry.COLUMN_RELATIVE_PHONE));
        mEmailEditText.setText(user.getEmail());
        mHomeAddressEditText.setText(user.getString(UserContract.UserEntry.COLUMN_HOME));

        mSexSpinner.setSelection(user.getInt(UserContract.UserEntry.COLUMN_SEX));
        mRelationSpinner.setSelection(user.getInt(UserContract.UserEntry.COLUMN_RELATIVE_RELATION));
    }

    private boolean isInfoValid() {
        return true;
    }

    private void attemptModify() {
        if (!isInfoValid()) {
            Toast.makeText(this, "请检查你的信息！", Toast.LENGTH_LONG).show();
            return;
        }
        AVUser user = AVUser.getCurrentUser();
        user.put(UserContract.UserEntry.COLUMN_REAL_NAME, mRealNameEditText.getText().toString());
        user.put(UserContract.UserEntry.COLUMN_AGE, Integer.parseInt(mAgeEditText.getText().toString()));
        user.put(UserContract.UserEntry.COLUMN_ID_CARD, mIdCardEditText.getText().toString());
        user.put(UserContract.UserEntry.COLUMN_PHONE, mPhoneEditText.getText().toString());
        user.put(UserContract.UserEntry.COLUMN_RELATIVE_NAME, mRelativeNameEditText.getText().toString());
        user.put(UserContract.UserEntry.COLUMN_RELATIVE_PHONE, mRelativePhoneEditText.getText().toString());
        user.put(UserContract.UserEntry.COLUMN_SCHOOL, mSchoolSpinner.getSelectedItem().toString());
        user.put(UserContract.UserEntry.COLUMN_SEX, mSexSpinner.getSelectedItemPosition());
        user.put(UserContract.UserEntry.COLUMN_RELATIVE_RELATION, mRelationSpinner.getSelectedItemPosition());
//        user.put(UserContract.UserEntry.COLUMN_EMAIL, mEmailEditText.getText().toString());
        user.setEmail(mEmailEditText.getText().toString());
        user.put(UserContract.UserEntry.COLUMN_HOME, mHomeAddressEditText.getText().toString());
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    setResult(Activity.RESULT_OK);
                    Toast.makeText(UserInfoActivity.this,
                            getString(R.string.user_info_modified),
                            Toast.LENGTH_LONG).show();
                    UserInfoActivity.this.finish();
                } else {
                    String json = e.getMessage();
                    JSONTokener tokener = new JSONTokener(json);
                    try{
                        JSONObject jsonObject = (JSONObject) tokener.nextValue();
                        Toast.makeText(UserInfoActivity.this,
                                jsonObject.getString("error"),
                                Toast.LENGTH_LONG).show();
                    }
                    catch (JSONException jse) {
                        jse.printStackTrace();
                    }
                }
            }
        });
    }
}
