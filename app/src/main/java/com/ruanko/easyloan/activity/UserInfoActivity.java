package com.ruanko.easyloan.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ruanko.easyloan.R;

public class UserInfoActivity extends AppCompatActivity {
    private String[] province = null;
    private String[] school_1 = null;
    private String[][] school = null;
    private Spinner province_spinner;
    private Spinner school_spinner;
    private Context context;
    ArrayAdapter<String> province_adapter ;
    ArrayAdapter<String> school_adapter;

    /**/
    private Spinner card_type=null;
    private ArrayAdapter adapter=null;
    private Spinner sex_person=null;
    private ArrayAdapter adapter1=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        context = this;
        province = this.getResources().getStringArray(R.array.province);
        school_1 = this.getResources().getStringArray(R.array.school);
        school = getTwoDimensionalArray(school_1);
    }

    /**
     * 二级学校
     */
    private AdapterView.OnItemSelectedListener selectListener = new AdapterView.OnItemSelectedListener(){
        public void onItemSelected(AdapterView parent, View v, int position, long id){
            int pos = province_spinner.getSelectedItemPosition();
            school_adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, school[pos]);//school[pos]
            school_spinner.setAdapter(school_adapter);
        }
        public void onNothingSelected(AdapterView arg0){
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
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

    /**
     *
     *证件类型单选框选择
     */
    private AlertDialog alertDialog1;
    public void selectTypeOfCard(View view){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("请选择证件类型");
        alertBuilder.setSingleChoiceItems(R.array.type_card, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                switch (index){
                    case 0:
                        EditText textView=(EditText)findViewById(R.id.type_card_text);
                        textView.setHint("身份证");
                        break;
                    case 1:
                        EditText textView1=(EditText)findViewById(R.id.type_card_text);
                        textView1.setHint("绿卡");
                        break;
                    case 2:
                        EditText textView2=(EditText)findViewById(R.id.type_card_text);
                        textView2.setHint("港澳通行证");
                        break;
                    case 3:
                        EditText textView3=(EditText)findViewById(R.id.type_card_text);
                        textView3.setHint("二十四期");
                        break;
                    default:
                        break;
                }
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //TODO 业务逻辑代码

                // 关闭提示框
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }
    /**
     *
     *性别单选框选择
     */
    private AlertDialog alertDialog2;
    public void selectSexOfUser(View view){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("请选择性别");
        alertBuilder.setSingleChoiceItems(R.array.sex_person, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                switch (index){
                    case 0:
                        EditText textView=(EditText)findViewById(R.id.sex_text);
                        textView.setHint("性别:男");
                        break;
                    case 1:
                        EditText textView1=(EditText)findViewById(R.id.sex_text);
                        textView1.setHint("性别:女");
                        break;
                    case 2:
                        EditText textView2=(EditText)findViewById(R.id.sex_text);
                        textView2.setHint("性别:保密");
                        break;
                    case 3:
                        EditText textView3=(EditText)findViewById(R.id.sex_text);
                        textView3.setHint("性别:不详");
                        break;
                    default:
                        break;
                }
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //TODO 业务逻辑代码

                // 关闭提示框
                alertDialog2.dismiss();
            }
        });
        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
    }
    /**
     *
     *地区选择单选框
     */
    private AlertDialog alertDialog3;
    public void selectprovince(View view){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("请选择地区");
        alertBuilder.setSingleChoiceItems(province, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                switch (index){
                    case 0:
                        EditText textView=(EditText)findViewById(R.id.province_text);
                        textView.setHint(province[0]);
                        alertDialog3.dismiss();
                        selectschool(0);
                        break;
                    case 1:
                        EditText textView1=(EditText)findViewById(R.id.province_text);
                        textView1.setHint(province[1]);
                        alertDialog3.dismiss();
                        selectschool(1);
                        break;
                    case 2:
                        EditText textView2=(EditText)findViewById(R.id.province_text);
                        textView2.setHint(province[2]);
                        alertDialog3.dismiss();
                        selectschool(2);
                        break;
                    case 3:
                        EditText textView3=(EditText)findViewById(R.id.province_text);
                        textView3.setHint(province[3]);
                        alertDialog3.dismiss();
                        selectschool(3);
                        break;
                    case 4:
                        EditText textView4=(EditText)findViewById(R.id.province_text);
                        textView4.setHint(province[4]);
                        alertDialog3.dismiss();
                        selectschool(4);
                        break;
                    default:
                        break;
                }
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //TODO 业务逻辑代码

                // 关闭提示框
                alertDialog3.dismiss();
            }
        });
        alertDialog3 = alertBuilder.create();
        alertDialog3.show();
    }
    /**
     *
     *性别单选框选择
     */
    private AlertDialog alertDialog4;
    public void selectschool(int i){

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("请选择学校");
        final int finalInd = i;
        alertBuilder.setSingleChoiceItems(school[i], 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int index) {
                switch (index){
                    case 0:
                        EditText textView=(EditText)findViewById(R.id.school_text);
                        textView.setHint(school[finalInd][0]);
                        break;
                    case 1:
                        EditText textView1=(EditText)findViewById(R.id.school_text);
                        textView1.setHint(school[finalInd][1]);
                        break;
                    case 2:
                        EditText textView2=(EditText)findViewById(R.id.school_text);
                        textView2.setHint(school[finalInd][2]);
                        break;
                    default:
                        break;
                }
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //TODO 业务逻辑代码

                // 关闭提示框
                alertDialog4.dismiss();
            }
        });
        alertDialog4 = alertBuilder.create();
        alertDialog4.show();
    }
}
