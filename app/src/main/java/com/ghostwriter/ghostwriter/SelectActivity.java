package com.ghostwriter.ghostwriter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectActivity extends AppCompatActivity {
    Button TButton;
    Button SButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

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

    }
}
