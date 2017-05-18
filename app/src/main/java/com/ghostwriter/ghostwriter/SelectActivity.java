package com.ghostwriter.ghostwriter;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import static com.ghostwriter.ghostwriter.R.id.toolbar;

public class SelectActivity extends AppCompatActivity {
    Button TButton;
    Button SButton;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        TButton = (Button)findViewById(R.id.Teacher);
        TButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);


            }
        });

        SButton = (Button)findViewById(R.id.Student);
        SButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SelectSubActivity.class);
                startActivity(intent);
                overridePendingTransition( R.anim.anim_slide_out_left,R.anim.anim_slide_in_right);

            }
        });
        mToolbar = (Toolbar)findViewById(toolbar);
        setSupportActionBar(mToolbar);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.first:
                Intent intent = new Intent(getApplicationContext(), settingActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}





