package com.ruanko.easyloan.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.ruanko.easyloan.R;

public class UserInfoActivity extends AppCompatActivity {
    private String[] province = null;
    private String[] school = null;
    private Spinner province_spinner;
    private Spinner school_spinner;
    private Context context;
    ArrayAdapter<String> school_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        context = this;
        province = this.getResources().getStringArray(R.array.province);
        school_spinner=(Spinner)findViewById(R.id.school_select);
        province_spinner=(Spinner)findViewById(R.id.prpovince_select);
        province_spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView parent, View v, int position, long id){
                int pos = province_spinner.getSelectedItemPosition();
                school=getSchool(pos);
                school_adapter = new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, school);//school[pos]
                school_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                school_spinner.setAdapter(school_adapter);
            }
            public void onNothingSelected(AdapterView arg0){
            }
        });
    }



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
     *
     *
     */
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

}
