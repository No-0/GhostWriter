package com.ghostwriter.ghostwriter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.Socket;


public class SelectSubActivity extends AppCompatActivity {
    static final String[] LIST_MENU = {"국어", "수학", "국사","사회문화","화학", "생명과학", "물리"} ;// 서버에서 진행중인 수업 리스트 불러오기
    public  static Context mContext;
    String strText;


//    String IPadr;
//    String PortN;
    ReceiveThread RT;
    ArrayAdapter adapter;
    String SName;
    ListView listview;

    Toolbar mToolbar;
    Socket socket;
    TextView show;
    Handler msghandler;

    String IPadr = "223.194.152.180"; //아이피주소
    String PortN =  "5000"; //포트번호


    public String GetText(){
        return strText;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mContext = this;


       adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;




        listview = (ListView) findViewById(R.id.listview) ;
        listview.setAdapter(adapter) ;


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                strText = (String) parent.getItemAtPosition(position) ;//선택된 과목명을 strText에 저장함

                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);

                // TODO : use strText
            }
        }) ;
        // Set a toolbar to  replace to action bar
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {//툴바 뒤로가기
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
    }




}
