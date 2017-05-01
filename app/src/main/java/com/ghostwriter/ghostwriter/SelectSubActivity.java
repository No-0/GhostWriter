package com.ghostwriter.ghostwriter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SelectSubActivity extends AppCompatActivity {
    static final String[] LIST_MENU = {"","",""} ;// 서버에서 진행중인 수업 리스트 불러오기
    String strText;

    public static Context mContext;
    public String GetText(){
        return strText;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;




        ListView listview = (ListView) findViewById(R.id.listview) ;
        listview.setAdapter(adapter) ;

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                // get TextView's Text.
                strText = (String) parent.getItemAtPosition(position) ;//선택된 과목명을 strText에 저장함

                Intent intent = new Intent(getApplicationContext(), StudentActivity.class);
                startActivity(intent);

                // TODO : use strText
            }
        }) ;

    }


}
