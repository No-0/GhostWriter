package com.ghostwriter.ghostwriter;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ImageView pictureim = (ImageView)findViewById(R.id.imageView);
        pictureim.setImageResource(R.drawable.simbol);

        ImageView pictureim2 = (ImageView)findViewById(R.id.imageView2);
        pictureim2.setImageResource(R.drawable.kakao_api_endorsedmark_screen);




        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(IntroActivity.this,SelectActivity.class);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}
