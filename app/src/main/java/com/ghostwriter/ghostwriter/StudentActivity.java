package com.ghostwriter.ghostwriter;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class StudentActivity extends AppCompatActivity {


    String IPadr;
    String PortN;
    SocketClient client_Server;
    SReceiveThread RT;


    String GETText;

    TextView SName;
    Socket socket;
    TextView show;
    Handler msghandler;
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        IPadr = "223.194.152.180"; //아이피주소

        show = (TextView) findViewById(R.id.show);


        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);




        GETText = ((SelectSubActivity) SelectSubActivity.mContext).GetText();


        getSupportActionBar().setTitle(GETText);//타이틀 과목명 변경
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//뒤로가기


        switch (GETText) {//받은 과목명에 따른 포트번호 할당, 현재는 임시 포트번호
            case "국어": {
                setStatusBarColor(this,0xffcc0000);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffcc0000));//빨강
                PortN = "5001";
            }
            break;
            case "수학": {
                setStatusBarColor(this,0xff00aecc);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff00aecc));//어두운 하늘
                PortN = "5002";
            }
            break;
            case "국사": {
                setStatusBarColor(this,0xff542004);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff542004));//고동색
                PortN = "5003";
            }
            break;
            case "사회문화": {
                setStatusBarColor(this,0xFF5F00FF);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF5F00FF));//군청
                PortN = "5004";
            }
            break;
            case "화학": {
                setStatusBarColor(this,0xffFF0057);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xffFF0057));//qkfr
                PortN = "5005";
            }
            break;
            case "생명과학": {
                setStatusBarColor(this,0xff878787);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff878787));//회색
                PortN = "5006";
            }
            break;
            case "물리": {
                setStatusBarColor(this,0xFF157D00);
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF157D00));//초록
                PortN = "5007";
            }
            break;
            default: {
                Toast.makeText(this, "Select Subject Error", Toast.LENGTH_LONG).show();
                //어플 종료
                moveTaskToBack(true);
                finish();
                Process.killProcess(Process.myPid());
            }
        }





        client_Server = new SocketClient(IPadr,PortN);
        client_Server.start();
        RT = new SReceiveThread(socket);
        RT.start();

        msghandler = new Handler() {
            @Override
            public void handleMessage(Message hdmsg) {
                if (hdmsg.what == 11111) {
                    show.append(hdmsg.obj.toString());
                }
            }
        };

        //폰트 크기 설정
        Button FontUp = (Button) findViewById(R.id.font_up);
        FontUp.setOnClickListener(new View.OnClickListener() {
            TextView textView = (TextView) findViewById(R.id.show);
            float textSize;

            @Override
            public void onClick(View v) {
                textSize = textView.getTextSize();
                if (textSize < 90) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize + 4);
                }
            }
        });

        Button FontDown = (Button) findViewById(R.id.font_down);
        FontDown.setOnClickListener(new View.OnClickListener() {
            float textSize;
            TextView textView = (TextView) findViewById(R.id.show);

            @Override
            public void onClick(View v) {
                textSize = textView.getTextSize();
                if (textSize > 18) {
                    textSize = textSize - 4;
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }
            }
        });



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



    class SocketClient extends Thread {
        boolean threadAlive;
        String ip;
        String port;
        String mac;

        //InputStream inputStream = null;
        BufferedReader outputStream = null;
        BufferedReader br = null;

        private DataOutputStream output = null;

        public SocketClient(String ip, String port) {
            threadAlive = true;
            this.ip = ip;
            this.port = port;
        }

        @Override
        public void run() {
            try {
                socket = new Socket(ip, Integer.parseInt(port));

                //inputStream = socket.getInputStream();
                output = new DataOutputStream(socket.getOutputStream());
                RT = new SReceiveThread(socket);
                RT.start();

                WifiManager mng = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                WifiInfo info = mng.getConnectionInfo();
                mac = info.getMacAddress();


                output.writeUTF(mac);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class SReceiveThread extends Thread {
        private Socket socket = null;
        BufferedReader input;

        public SReceiveThread(Socket socket) {
            this.socket = socket;
            try {
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
;
            } catch (Exception e) {
            }
        }

        public void run() {
            try {
                while (input != null) {
                    String msg = input.readLine();
                    if (msg != null) {
                        Log.d(ACTIVITY_SERVICE, "test");

                        Message hdmsg = msghandler.obtainMessage();
                        hdmsg.what = 11111;
                        hdmsg.obj = msg;
                        msghandler.sendMessage(hdmsg);
                        Log.d(ACTIVITY_SERVICE, hdmsg.obj.toString());

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }




    public static void setStatusBarColor(Activity activity, int color) {// 상태바 색상 변경함수
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }
}